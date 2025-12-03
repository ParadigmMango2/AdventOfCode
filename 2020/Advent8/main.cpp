#include <iostream>
#include <fstream>
#include <array>
#include <cstdlib>
#include <cstring>

struct Instruction {
  Instruction() = default;

  Instruction(char string[4], int i) {
    strcpy(op, string);
    arg = i;
  }

  char op[4];
  int arg;
  bool executed = false;
};

std::array<Instruction, 636> ParseInput() {
  std::array<Instruction, 636> instructions;

  std::ifstream in("input.txt");

  for (int i = 0; i < 636; ++i) {
    char op[4];
    char arg_c[5];

    in >> op;
    in >> arg_c;

    instructions[i] = Instruction(op, atoi(arg_c));
  }

  return instructions;
}

bool DoesChangeFix(int change_line, const std::array<Instruction, 636> &instructions) {
  std::array<Instruction, 636> local_instructions = instructions;

  int line = 0;
  int acc = 0;

  switch (local_instructions[change_line].op[0]) {
    case 'n':
      strcpy(local_instructions[change_line].op, "jmp");
      break;
    case 'j':
      strcpy(local_instructions[change_line].op, "nop");
      break;
    default:
      break;
  }

  while (!local_instructions[line].executed && line < 636 && line >= 0) {
    local_instructions[line].executed = true;

    switch (local_instructions[line].op[0]) {
      case 'a':
        acc += local_instructions[line++].arg;
        break;
      case 'j':
        line += local_instructions[line].arg;
        break;
      default:
        line++;
        break;
    }
  }

  std::cout << acc << '\n';

  return line == 636;
}

int main() {
  std::array<Instruction, 636> instructions = ParseInput();

  int change_line = 0;

  while (!DoesChangeFix(change_line++, instructions));

  return 0;
}
