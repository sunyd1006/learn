from util import Util
from util import FileUtil

if __name__ == '__main__':
    print(Util.runcmd_success("ls -a"))

    Util.runcmd("touch /tmp/abc.log")
    FileUtil.backup_path("/tmp/abc.log")
    print(Util.runcmd_success("ls /tmp/abc*"))


