#include <iostream>
#include <fstream>
#include <array>
#include <algorithm>

struct Adapter {
  Adapter() = default;

  bool operator<(const Adapter &rhs) const {
    return val < rhs.val;
  }

  int val;
  long long paths_from = 0;
};

std::array<Adapter, 93> ParseInput() {
  std::ifstream in("input.txt");

  std::array<Adapter, 93> joltages;
  joltages[0].val = 0;

  for(int i = 1; i < 93; ++i) {
    int joltage;

    in >> joltage;

    joltages[i].val = joltage;
  }

  return joltages;
}

std::array<int, 3> CountDifferences(std::array<Adapter, 92> joltages) {
  int ones = 0, twos = 0, threes = 1;

  std::sort(joltages.begin(), joltages.end());

  switch (joltages[0].val) {
    case 1:
      ++ones;
      break;
    case 2:
      ++twos;
      break;
    case 3:
      ++threes;
      break;
  }

  for (int i = 1; i < 92; ++i) {
    switch (joltages[i].val - joltages[i - 1].val) {
      case 1:
        ++ones;
        break;
      case 2:
        ++twos;
        break;
      case 3:
        ++threes;
        break;
    }
  }

  return {ones, twos, threes};
}

bool ValidateAdapter(const std::array<Adapter, 93> &joltages, const int &current_adapter, const int &k) {
  return (joltages[current_adapter + k].val - joltages[current_adapter].val <= 3);
}

long long CountAdapterArrangements(std::array<Adapter, 93> &joltages) {
  joltages[92].paths_from = 1LL;
  joltages[91].paths_from = 1LL;
  joltages[90].paths_from = 2LL;

  for (int i = 89; i > -1; --i)
    for (int k = 1; k < 4; ++k)
      if (ValidateAdapter(joltages, i, k))
        joltages[i].paths_from += joltages[i + k].paths_from;

  return joltages[0].paths_from;
}

int main() {
  std::array<Adapter, 93> joltages = ParseInput();

  std::sort(joltages.begin(), joltages.end());

  std::cout << CountAdapterArrangements(joltages);

  return 0;
}
