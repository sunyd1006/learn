# AGENTS.md

This document provides a high-level overview of the codebase architecture and structure to help agents and users quickly understand the project.

## 1. Project Overview

This repository is a comprehensive learning environment with a variety of code examples and projects in multiple programming languages. The primary focus is on C++, but it also includes significant code for Java, Go, Python, and machine learning. The repository is structured as a collection of individual projects and examples, rather than a single application.

## 2. Build & Commands

The primary build system for C++ projects is CMake.

### Building C++ Projects

To build the main C++ executable, follow these steps:
```bash
mkdir build
cd build
cmake ..
make
```

### Running Tests and Examples

Many directories contain specific scripts to run tests or examples. Here are a few key scripts:

- `test_cpp/code_snippet/run.sh`: Runs standalone C++ code examples.
- `test_cpp/test_gtest/run.sh`: Executes Google Test examples.
- `test_cpp/test_jni/run.sh`: Runs Java Native Interface (JNI) examples.

## 3. Code Style

While there are no explicitly defined coding style guidelines in the repository, the existing code generally follows standard conventions for each language. When contributing, it is recommended to follow the style of the existing code in the relevant directory.

## 4. Testing

The repository uses Google Test for C++ testing. The main test files are located in `test_cpp/test_gtest/`. To run the tests, use the `run.sh` script in that directory.

## 5. Security

There are no specific security guidelines documented in the repository. However, as a general practice, be mindful of the following:
- Avoid hardcoding sensitive information such as passwords or API keys.
- Be cautious when handling user input to prevent common vulnerabilities like injection attacks.
- Keep dependencies up-to-date to avoid known vulnerabilities.

## 6. Configuration

The repository contains environment setup scripts in the `1env_install/` directory. These scripts can be used to configure a development environment for the various languages and tools used in the repository. For specific configurations, refer to the `README.md` files within the subdirectories of `1env_install/`.
