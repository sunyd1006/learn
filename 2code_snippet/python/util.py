#!/bin/bash/python3
import os
import subprocess
import json

# 执行 shell 命令
class Util:
    @staticmethod
    def run_shell_command(command, print_result=False):
        try:
            print("willrun: " , command)
            # 调用 shell 命令
            result = subprocess.check_output(command, cwd= shell_working_dir, shell=True, universal_newlines=True, stderr=subprocess.STDOUT)
            if print_result:
                print(result.strip())
            return result.strip()  # .strip() 用于去掉输出末尾的换行符
        except subprocess.CalledProcessError as e:
            # 处理错误
            print("Command failed with return code:", e.returncode)
            print("Command output:", e.output.strip())  # 输出错误信息
            return None

    @staticmethod
    def runcmd(cls, cmd):
        print("runcmd %s ..." % cmd)
        process = subprocess.Popen(cmd, stdout = subprocess.PIPE, shell = True, stderr = subprocess.PIPE)
        while True:
            output = process.stdout.readline()
            if output == '' and process.poll() is not None:
                break
            if output:
                print(output.strip())
        (_, error) = process.communicate()
        if process.returncode != 0:
            print("runcmd %s failed, %d, %s" % (cmd, process.returncode, error))
        return process.returncode, error

    @classmethod
    def runcmd_success(cls, cmd):
        (code, error) = cls.runcmd(cmd)
        if code != 0:
            raise Exception("execute %s failed, %s" % (cmd, error))

class FileUtil:
    @classmethod
    def read_json(cls, json_path):
        with open(json_path, 'r') as fp:
            content = json.load(fp)
            return content

    @classmethod
    def dump_json(cls, file_path, json_struct, indent=4):
        json_str = json.dumps(json_struct, indent, sort_keys=True)
        cls.write_file(file_path, json_str)

    @classmethod
    def write_file(cls, file_path, content):
        """ Automatically create parent directories, and write content to the file. """
        dirname = os.path.dirname(file_path)
        if not os.path.exists(dirname):
            os.makedirs(dirname)
        with open(file_path, 'w') as f:
            f.write(str(content))

    @classmethod
    def append_file(cls, file_path, content):
        with open(file_path, 'a') as f:
            f.write(str(content))

    @classmethod
    def read_file(cls, file_path):
        with open(file_path, 'r') as f:
            return f.read()

    @classmethod
    def remove_file(cls, file_path):
        try:
            # 删除文件
            os.remove(file_path)
            print(f"File '{file_path}' has been deleted successfully.")
        except FileNotFoundError:
            print(f"File '{file_path}' not found.")
        except PermissionError:
            print(f"Permission denied: Unable to delete '{file_path}'.")
        except Exception as e:
            print(f"An error occurred: {e}")

