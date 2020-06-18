cmake_minimum_required(VERSION 3.7)
project(poppler-jni)

set(CMAKE_CXX_STANDARD 11)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})
include_directories(/usr/local/include/poppler/cpp)

add_library(poppler-jni SHARED
    src/main/cpp/edu_illinois_library_poppler_PopplerDocument.cpp
    src/main/cpp/edu_illinois_library_poppler_PopplerPage.cpp
    src/main/cpp/edu_illinois_library_poppler_PopplerPageRenderer.cpp)

find_library(POPPLER_CPP_LIB poppler-cpp)
target_link_libraries(poppler-jni "${POPPLER_CPP_LIB}")
