function print_center() {
    str="========================="
    echo -e "\n"$str" "$1" "$str"\n"
}

scons -Q -c

print_center "run start"

./build/ex26

print_center "run done"


rm -rf  *.o *.out *.os

# rm -rf *.a *.so 