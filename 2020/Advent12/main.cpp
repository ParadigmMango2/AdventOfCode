#include <iostream>
#include <fstream>
#include <array>

class Ship {
 public:
  Ship() = default;

  void ExecuteInstruction(std::pair<char, int> instruction) {
    switch (instruction.first) {
      case 'L':
        TurnLeft(instruction.second);
        break;
      case 'R':
        TurnRight(instruction.second);
        break;
      case 'F':
        MoveForward(instruction.second);
        break;
      default:
        MoveInDirection(instruction.first, instruction.second);
    }
  }

  int GetX() const {
    return x;
  }

  int GetY() const {
    return y;
  }

 private:
  int x = 0, y = 0; // East = +x
  int waypoint_x = 10, waypoint_y = 1;
  char direction = 'E';

  void MoveInDirection(const char &dir, const int &val) {
    switch (dir) {
      case 'N':
        waypoint_y += val;
        break;
      case 'E':
        waypoint_x += val;
        break;
      case 'S':
        waypoint_y -= val;
        break;
      case 'W':
        waypoint_x -= val;
        break;
    }
  }

  void MoveForward(const int &val) {
    x += waypoint_x * val;
    y += waypoint_y * val;
  }

  void TurnLeft(const int &angle) {
    const int times = angle / 90;

    for (int i = 0; i < times; ++i) {
      const int tmp = waypoint_x;
      waypoint_x = -waypoint_y;
      waypoint_y = tmp;
    }
  }

  void TurnRight(const int &angle) {
    const int times = angle / 90;

    for (int i = 0; i < times; ++i) {
      const int tmp = waypoint_x;
      waypoint_x = waypoint_y;
      waypoint_y = -tmp;
    }
  }
};

std::array<std::pair<char, int>, 783> ParseInput() {
  std::ifstream in("input.txt");

  std::array<std::pair<char, int>, 783> instructions;

  for (int i = 0; i < 783; ++i) {
    char action;
    int value;

    in >> action;
    in >> value;

    instructions[i] = {action, value};
  }

  return instructions;
}

int main() {
  std::array<std::pair<char, int>, 783> instructions = ParseInput();
  Ship ship;

  for (std::pair<char, int> instruction : instructions)
    ship.ExecuteInstruction(instruction);

  std::cout << abs(ship.GetX()) + abs(ship.GetY());

  return 0;
}
