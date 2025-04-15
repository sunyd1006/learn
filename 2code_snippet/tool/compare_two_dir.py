import os
import filecmp
from hashlib import md5

def get_file_hash(file_path):
    """计算文件的 MD5 哈希值，用于精确比较文件内容"""
    hash_md5 = md5()
    with open(file_path, "rb") as f:
        for chunk in iter(lambda: f.read(4096), b""):  # 逐块读取文件
            hash_md5.update(chunk)
    return hash_md5.hexdigest()

def compare_directories(dir1, dir2):
    """递归比较两个目录中的文件和子目录，输出不同的文件"""
    onlyLeft, differences, onlyRight = [], [], []
    # 使用 filecmp 比较文件和子目录
    comparison = filecmp.dircmp(dir1, dir2)

    # 比较相同文件夹中的相同文件
    for file in comparison.common_files:
        file1 = os.path.join(dir1, file)
        file2 = os.path.join(dir2, file)
        if not compare_files(file1, file2):
            differences.append(file2)
            # differences.append(file2 + "  \n\t" + file1)

    # 输出只存在于 dir1 或 dir2 中的文件或文件夹
    for file in comparison.left_only:
        onlyLeft.append(os.path.join(dir1, file))
    for file in comparison.right_only:
        onlyRight.append(os.path.join(dir2, file))

    # 递归比较子文件夹
    for subdir in comparison.common_dirs:
        subdir1 = os.path.join(dir1, subdir)
        subdir2 = os.path.join(dir2, subdir)
        a, b, c = compare_directories(subdir1, subdir2)
        onlyLeft.extend(a)
        differences.extend(b)
        onlyRight.extend(c)
    return onlyLeft, differences, onlyRight

def compare_files(file1, file2):
    """比较两个文件是否相同（根据文件大小、修改时间和哈希值）"""
    # 如果文件大小不同，直接返回 False
    if os.path.getsize(file1) != os.path.getsize(file2):
        return False

    # 如果文件的修改时间不同，也返回 False
    if os.path.getmtime(file1) != os.path.getmtime(file2):
        return False

    # 比较文件内容的哈希值
    return get_file_hash(file1) == get_file_hash(file2)

# 示例目录路径
dir1 = '/Users/sunyindong.syd/codespace/learn/1env_install/new_mac'
dir2 = '/Users/sunyindong.syd/codespace/learn/1env_install/new_machine'

def printFunc(list):
    for file in list:
        if "target/classes" in file:
            continue
        if ".gradle" in file:
            continue
        print(file)

# 获取不同的文件列表
l, d, r = compare_directories(dir1, dir2)
if l:
    print(f"\n\nOnly in dir1: {dir1}:")
    printFunc(l)
if r:
    print(f"\n\nOnly in dir2: {dir2}:")
    printFunc(r)
if d:
    print("\n\nDifferences:")
    printFunc(d)
