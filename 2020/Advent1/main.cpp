#include <iostream>
#include <fstream>

int main() {
  int input[200];

  // Move nums to input
  std::ifstream in("../input.txt");
  int num;

  for (int i = 0; i < 200; i++) {
    in >> num;
    input[i] = num;
  }
  in.close();

  // Search and print solution
  for (int i = 0; i < 200; i++) {
    for (int j = i + 1; j < 200; j++) {
      for (int k = j + 1; k < 200; k++) {
        if (input[i] + input[j] + input[k] == 2020) {
          std::cout << input[i] * input[j] * input[k];
          return 0;
        }
      }
    }
  }

  return 1;
}
