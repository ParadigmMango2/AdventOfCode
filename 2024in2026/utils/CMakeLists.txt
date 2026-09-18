find_package(scn CONFIG REQUIRED)
add_library(aoc_utils STATIC src/utils.cc)
target_include_directories(aoc_utils PUBLIC include)
target_link_libraries(aoc_utils PUBLIC scn::scn)
