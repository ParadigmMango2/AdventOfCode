#include <iostream>
#include <fstream>
#include <vector>
#include <stdlib.h>

struct Field {
  std::string name;
  std::string value;
};

std::string GetNextPassport(std::ifstream &ifstream) {
  std::string passport;

  while (ifstream.peek() != '\n' && !ifstream.eof()) {
    char line[200];
    ifstream.getline(line, 200, '\n');

    passport.append(line);
    passport.append(" ");
  }

  ifstream.get();

  return passport;
}

std::vector<Field> GetNextPassportFields(std::ifstream &ifstream) {
  std::string passport = GetNextPassport(ifstream);

  std::vector<Field> fields;
  std::vector<int> colon_indices;
  std::vector<int> space_indices;

  for (int i = 0; i < passport.length(); i++) {
    if (passport[i] == ':')
      colon_indices.push_back(i);
    else if (passport[i] == ' ')
      space_indices.push_back(i);
  }

  for (int i = 0; i < colon_indices.size(); i++) {
    fields.push_back({passport.substr(colon_indices[i] - 3, 3),
                      passport.substr(colon_indices[i] + 1, space_indices[i] - colon_indices[i] - 1)});
  }

  return fields;
}

int main() {
  int valid_passports = 0;
  std::ifstream in("input.txt");

  while (!in.eof()) {
    std::vector<Field> fields = GetNextPassportFields(in);
    bool has_byr = false, has_iyr = false, has_eyr = false, has_hgt = false, has_hcl = false, has_ecl = false, has_pid = false;

    for (const Field &field : fields) {
      if (field.name == "byr") {
        int byr = atoi(field.value.c_str());

        if (1920 <= byr && byr <= 2002)
          has_byr = true;
      } else if (field.name == "iyr") {
        int iyr = atoi(field.value.c_str());

        if (2010 <= iyr && iyr <= 2020)
          has_iyr = true;
      } else if (field.name == "eyr") {
        int eyr = atoi(field.value.c_str());

        if (2020 <= eyr && eyr <= 2030)
          has_eyr = true;
      } else if (field.name == "hgt") {
        if (field.value[3] == 'c') {
          int hgt = atoi(field.value.substr(0, 3).c_str());
          if (150 <= hgt && hgt <= 193)
            has_hgt = true;
        } else if (field.value[3] == 'n') {
          int hgt = atoi(field.value.substr(0, 2).c_str());
          if (59 <= hgt && hgt <= 76)
            has_hgt = true;
        }
      } else if (field.name == "hcl") {
        if (field.value[0] == '#' && field.value.length() == 7) {
          std::string hex_color = field.value.substr(1, 6);

          try {
            std::stoi(hex_color, 0, 16);
            has_hcl = true;
          } catch (std::invalid_argument) {}
        }
      } else if (field.name == "ecl") {
        if (field.value == "amb" || field.value == "blu" || field.value == "brn" || field.value == "gry"
            || field.value == "grn" || field.value == "hzl" || field.value == "oth")
          has_ecl = true;
      } else if (field.name == "pid") {
        if (field.value.length() == 9)
          try {
            std::stol(field.value.c_str(), 0, 10);
            has_pid = true;
          } catch (std::invalid_argument) {}
      }
    }

    if (has_byr && has_iyr && has_eyr && has_hgt && has_hcl && has_ecl && has_pid)
      valid_passports++;
  }

  std::cout << valid_passports;

  return 0;
}
