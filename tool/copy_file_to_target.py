import shutil
import os

# 定义源路径和目标路径
source_files = [
    "/Users/sunyindong/codespace/learnbak/TestJava/Leetcode/src/main/java/com/leetcode/test/T2.java",
    "/Users/sunyindong/codespace/learnbak/test_cpp/code_snippet/producer_consumer.cpp",
    "/Users/sunyindong/codespace/learnbak/test_cpp/code_snippet/thread_pool.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/jiu_kun.md",
    "/Users/sunyindong/codespace/learnbak/tmp_code/main",
    "/Users/sunyindong/codespace/learnbak/tmp_code/so.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/solution.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/solution1.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/solution2.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/solution3.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/solution4.cpp",
    "/Users/sunyindong/codespace/learnbak/tmp_code/thread_poo.cpp"
]

target_base_dir = "/Users/sunyindong/codespace/learn"

def copy_files(source_files, target_base_dir):
    for source_file in source_files:
        # 获取源文件的相对路径
        relative_path = os.path.relpath(source_file, start="/Users/sunyindong/codespace/learnbak")

        # 目标文件路径
        target_file = os.path.join(target_base_dir, relative_path)

        # 确保目标文件夹存在
        target_dir = os.path.dirname(target_file)
        os.makedirs(target_dir, exist_ok=True)

        # 复制文件
        shutil.copy2(source_file, target_file)  # copy2 保留文件的元数据

        print(f"Copied {source_file} to {target_file}")

# 执行文件复制
copy_files(source_files, target_base_dir)