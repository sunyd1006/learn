#!/bin/bash/python3
import signal
import os
import subprocess
import json
import datetime

# 执行 shell 命令
class Util:
    @classmethod
    def runcmd(cls, cmd, print_err=True, tips=''):
        try:
            process = subprocess.Popen(cmd, stdout=subprocess.PIPE, shell=True, stderr=subprocess.PIPE)
            (output, err) = process.communicate()
            if process.returncode != 0 and print_err:
                print(tips, output.decode('utf-8'))
            return process.returncode, output, err
        except KeyboardInterrupt:
            os.kill(process.pid, signal.SIGINT)

    @classmethod
    def runcmd_success(cls, cmd, print_out=True):
        (code, stdout, stderr) = cls.runcmd(cmd)
        if code != 0:
            raise Exception("execute %s failed, stdout: %s, stderr: %s" % (cmd, stdout, stderr))
        return stdout.decode('utf-8')

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

    @classmethod
    def backup_path(cls, path):
        if os.path.exists(path):
            filename = "{}_{}".format(path, TimeUtil.get_timestr())
            os.rename(path, filename)


class TimeUtil:

    # return 202505241333
    # py2, py3
    @classmethod
    def get_timestr(cls):
        return datetime.datetime.now().strftime('%Y%m%d%H%M')
