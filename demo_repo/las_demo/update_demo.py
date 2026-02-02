# 请在 LAS 开发机中执行以下代码

"""
------------------------------------------------------------------------------------------------
配置环境变量
------------------------------------------------------------------------------------------------
"""

import os
import uuid

import daft
import lance
import pyarrow as pa
from daft.io import IOConfig
from daft.io.lance import merge_columns
from daft.las.io import TOSConfig

def load_env_file(env_path: str) -> None:
    if not os.path.isfile(env_path):
        return
    with open(env_path, "r", encoding="utf-8") as env_file:
        for raw_line in env_file:
            line = raw_line.strip()
            if not line or line.startswith("#") or "=" not in line:
                continue
            key, value = line.split("=", 1)
            key = key.strip()
            value = value.strip().strip('"').strip("'")
            if key and key not in os.environ:
                os.environ[key] = value


load_env_file(".venv")

required_keys = ["LAS_TOS_ACCESS_KEY", "LAS_TOS_SECRET_KEY", "TOS_ENDPOINT", "REGION"]
missing_keys = [key for key in required_keys if not os.getenv(key)]
if missing_keys:
    raise ValueError(f"缺少环境变量: {', '.join(missing_keys)}")

ak = os.environ["LAS_TOS_ACCESS_KEY"]
sk = os.environ["LAS_TOS_SECRET_KEY"]
endpoint = os.environ["TOS_ENDPOINT"]
region = os.environ["REGION"]

"""
------------------------------------------------------------------------------------------------
读取数据
------------------------------------------------------------------------------------------------
"""
dataset_name = "ds_public"
df = daft.read_las_dataset(name=dataset_name)
df.show()


"""
------------------------------------------------------------------------------------------------
加列：此处仅展示该如何使用 daft 对 Lance 进行加列，逻辑非常简单
如果您有更复杂、更高级的需求，您可以参考 LAS 算子广场的算子进行操作
------------------------------------------------------------------------------------------------
"""

io_config = IOConfig(s3=TOSConfig.from_env().to_s3_config())
dataset_tos_path = "tos://las-ai-cn-beijing-online/las/lance/ds_2rrwvy00my56nbg05540.lance"


def get_create_tag_new_column_func(input_col: str, output_col: str):
    def tag_new_column(batch: pa.RecordBatch) -> pa.RecordBatch:
        values = batch.column(input_col).to_pylist()

        tagged_data_list = []
        for value in values:
            tagged_value = value + "__tag"
            tagged_data_list.append(tagged_value)

        tagged_data_array = pa.array(tagged_data_list, type=pa.string())
        new_batch = pa.RecordBatch.from_arrays([tagged_data_array], names=[output_col])

        # 仅返回新列
        return new_batch

    return tag_new_column


print("正在添加新列至 lance 数据集 ...")
merge_columns(
    url=dataset_tos_path.replace("tos://", "s3://"),
    io_config=io_config,
    transform=get_create_tag_new_column_func("title", "title_tag"),
)

print("新列添加完毕")


"""
------------------------------------------------------------------------------------------------
加列：如果您想使用我们 LAS 数据集页面上的完整功能，可以添加一个 __data_item_id 列作为逻辑主键
------------------------------------------------------------------------------------------------
"""


def create_assign_uuid(batch: pa.RecordBatch) -> pa.RecordBatch:
    num_rows = batch.num_rows

    uuid_list = list(range(num_rows))
    uuid_array = pa.array(uuid_list, type=pa.int64())

    return pa.RecordBatch.from_arrays([uuid_array], names=["__data_item_id"])


print("正在生成业务主键 ...")
merge_columns(
    url=dataset_tos_path.replace("tos://", "s3://"),
    io_config=io_config,
    transform=create_assign_uuid,
)

print("业务主键生成完毕 ...")


"""
------------------------------------------------------------------------------------------------
您也可以直接通过 lance sdk 来访问您的数据集，来查看更多 lance 格式的元信息，此处仅对部分接口进行展示
------------------------------------------------------------------------------------------------
"""


bucket_name = dataset_tos_path.removeprefix("tos://").split("/", 1)[0]
ds = lance.LanceDataset(
    uri=dataset_tos_path.replace("tos://", "s3://"),
    storage_options={
        "access_key_id": ak,
        "secret_access_key": sk,
        "aws_endpoint": f"https://{bucket_name}.tos-s3-{region}.ivolces.com",
        "virtual_hosted_style_request": "true",
    },
)

# 查看 Lance 数据集的 schema 信息
print("Lance 数据集的 schema 信息如下：")
print(ds.schema)

# 查看 Lance 数据集的版本信息
print("Lance 数据集的版本信息如下：")
print(ds.versions())

# 查看 Lance 数据集的索引信息
print("Lance 数据集的索引信息如下：")
print(ds.list_indices())
