# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("taglib_android")

# Include taglib subproject; exposes "tag" library
add_subdirectory(taglib)

# Define our Android JNI bridge library and link it with taglib
add_library(${CMAKE_PROJECT_NAME} SHARED taglib_jni.cpp)
target_link_libraries(${CMAKE_PROJECT_NAME} tag log)
target_include_directories(${CMAKE_PROJECT_NAME}
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/taglib
    ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib
    ${CMAKE_CURRENT_SOURCE_DIR}/taglib/taglib/toolkit
)
