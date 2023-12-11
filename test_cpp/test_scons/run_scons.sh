
function print_center() {
    str="========================="
    echo -e "\n"$str" "$1" "$str"\n"
}


scons -Q


print_center "run start"


./build/ex_lib_target

print_center "run done"

./build/ex7_main
print_center "run done"


rm -rf  *.o *.out *.os 

# rm -rf *.a *.so 