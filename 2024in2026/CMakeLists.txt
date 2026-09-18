#include(FetchContent)
#FetchContent_Declare(
#        scn
#        GIT_REPOSITORY https://github.com/eliaskosunen/scnlib.git
#        GIT_TAG v4.0.1
#)
#FetchContent_MakeAvailable(scn)

cmake_minimum_required(VERSION 3.20)
project(AdventOfCode2025 CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_subdirectory(utils)

# Find every Day* folder and make it an executable target
file(GLOB DAY_DIRS LIST_DIRECTORIES true CONFIGURE_DEPENDS
        ${CMAKE_CURRENT_SOURCE_DIR}/day*)

foreach(dir ${DAY_DIRS})
    if(IS_DIRECTORY ${dir} AND EXISTS ${dir}/main.cc)
        get_filename_component(day ${dir} NAME)
        add_executable(${day} ${dir}/main.cc)
        target_link_libraries(${day} PRIVATE aoc_utils scn)
        # Absolute path to this day's input folder, baked in at compile time
        target_compile_definitions(${day} PRIVATE
                INPUT_DIR="${dir}/input/")
    endif()
endforeach()
