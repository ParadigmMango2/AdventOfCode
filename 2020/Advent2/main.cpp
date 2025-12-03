#include <iostream>
#include <fstream>
#include <stdlib.h>

int main() {
  int num_valid = 0;

  std::ifstream in("input.txt");

  for (int entry = 0; entry < 1000; entry++) {
    char min_str[4];
    int min, max;
    char letter;
    char password[80];

    in.get(min_str, 4, '-');
    min = atoi(min_str);

    in.get();
    in >> max;

    in.get();
    in >> letter;

    in.seekg(2, in.cur);
    in >> password;

//    std::cout << min << ' ' << max << ' ' << letter << ' ' << password << ' ';

//    int letter_count = 0;
//    char *cursor = password;
//    while (*cursor) {
//      if (*cursor == letter)
//        letter_count++;
//
//      cursor++;
//    }
//
//    if (min <= letter_count && letter_count <= max)
//      num_valid++;

    if ((password[min - 1] == letter) != (password[max - 1] == letter))
      num_valid++;
  }

  std::cout << num_valid;

  return 0;
}
