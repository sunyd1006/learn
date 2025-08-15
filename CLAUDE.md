# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is a learning repository containing code examples and tests in multiple programming languages, with a primary focus on C++. The repository includes examples for:
- C++ concepts and features
- CMake build system tutorials
- Bash scripting examples
- Go, Java, Python, and other language examples
- System design patterns and models

## Build System

The repository uses CMake as its primary build system. The main CMakeLists.txt file is located at the root of the repository and includes:
- C++17 standard requirement
- Include directories for system headers
- Recursive globbing of C++ source files from test_cpp, tmp_cpp, and 2code_snippet directories
- Single executable target named "learn"

## Common Development Tasks

### Building the Project
```bash
mkdir build && cd build
cmake ..
make
```

### Running Tests
Individual C++ examples can be run directly after compilation. Some directories contain specific run scripts:
- `test_cpp/code_snippet/run.sh` - Runs code snippet examples
- `test_cpp/test_gtest/run.sh` - Runs Google Test examples
- `test_cpp/test_jni/run.sh` - Runs JNI examples

### Adding New Code
1. Place new C++ files in appropriate directories (test_cpp/, tmp_cpp/, or 2code_snippet/)
2. Run CMake to automatically include new files in the build
3. Build with `make` command

## Repository Structure
- `test_cpp/` - Primary directory for C++ examples and tutorials
- `test_bash/` - Bash scripting examples
- `test_go/` - Go language examples
- `test_java/` - Java language examples
- `test_grpc/` - gRPC examples
- `design_model/` - System design patterns
- `ml_repo/` - Machine learning resources
- `1env_install/` - Environment setup scripts
- `0lib/` - Utility libraries and common functions

## Key Directories for C++ Development
- `test_cpp/test_gtest/` - Google Test framework examples
- `test_cpp/test_cmake/` - CMake tutorial examples
- `test_cpp/code_snippet/` - Standalone C++ code examples
- `test_cpp/test_concurrent/` - Concurrency examples
- `test_cpp/test_primer/` - C++ Primer book examples