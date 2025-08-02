# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Codebase Overview

This is a personal learning repository containing code snippets and examples across multiple programming languages and domains:

1. **C++ Programming** - Extensive examples in `2code_snippet/cpp/` and `test_cpp/` focusing on:
   - Concurrent programming and threading
   - C++ fundamentals and advanced features
   - Build systems (CMake, SCons)
   - Testing with Google Test (gtest)

2. **Go Programming** - Examples in `2code_snippet/go/`

3. **Python** - Scripts and utilities in `2code_snippet/python/`

4. **Machine Learning/Deep Learning** - Comprehensive materials in `ml_repo/` including:
   - Dive into Deep Learning (D2L) book content
   - Jupyter notebooks for various ML frameworks (PyTorch, TensorFlow, MXNet, PaddlePaddle)
   - GPU programming examples

## Common Commands

### Building C++ Code
```bash
# For general C++ code using the root CMakeLists.txt
mkdir build && cd build
cmake ..
make

# For specific test directories (e.g., gtest)
cd test_cpp/test_gtest
./run.sh

# For code snippets
cd test_cpp/code_snippet
./run.sh
```

### Running Python ML Code
```bash
# Install dependencies for D2L PyTorch version
cd ml_repo/d2l-zh/pytorch
./install.sh

# Run Jupyter notebooks
jupyter notebook
```

### Go Development
```bash
# Standard Go commands
go build
go run main.go
go test
```

### Testing
```bash
# C++ Google Test
cd test_cpp/test_gtest
./run.sh

# The build process will automatically execute tests
```

## Codebase Structure

- `2code_snippet/` - Language-specific code examples (cpp, go, python)
- `test_cpp/` - Comprehensive C++ learning materials with focus on:
  - Testing (gtest)
  - Build systems (cmake, scons)
  - Concurrent programming
  - C++ primer examples
- `ml_repo/` - Machine learning and deep learning materials:
  - D2L book content in multiple frameworks
  - Jupyter notebooks covering ML concepts
- `1env_install/` - Environment setup scripts
- `tmp_cpp/` - Temporary C++ files for experimentation

## Development Notes

- The repository uses CMake as the primary build system for C++ code
- Python ML code requires specific package versions (see requirements.txt)
- Most directories have their own build/run scripts
- The codebase is primarily for personal learning and experimentation