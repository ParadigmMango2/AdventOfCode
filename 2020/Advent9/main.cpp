#include <iostream>
#include <fstream>
#include <array>

std::array<long, 1000> ParseInput() {
  std::ifstream in("input.txt");
  std::array<long, 1000> nums;

  for (int i = 0; i < 1000; ++i) {
    long line;
    in >> line;

    nums[i] = line;
  }

  return nums;
}

bool IsSpecialSum(int index, const std::array<long, 1000> &nums) { //776203571
  for (int i = index - 25; i < index; ++i)
    for (int j = i + 1; j < index; ++j)
      if (nums[i] + nums[j] == nums[index])
        return true;

  return false;
}

std::array<int, 2> FindSumSeries(long target, const std::array<long, 1000> &nums) {
  int start, end;

  for (start = 0; start < 999; ++start) {
    end = start + 1;
    long sum = nums[start] + nums[end];

    while (sum < target)
        sum += nums[++end];

    if (sum == target) break;
  }

  return {start, end};
}

int main() {
  std::array<long, 1000> nums = ParseInput();

  std::array<int, 2> sum_range = FindSumSeries(776203571l, nums);

  long largest, smallest;
  largest = smallest = nums[sum_range[0]];
  for (int i = sum_range[0] + 1; i <= sum_range[1]; ++i) {
    if (nums[i] > largest)
      largest = nums[i];
    else if (nums[i] < smallest)
      smallest = nums[i];
  }

  std::cout << largest + smallest;

  return 0;
}
