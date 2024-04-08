#include <stdio.h>

extern "C"
void sayHello(char* s) {
  printf("Hello %s\n", s);
}

extern "C"
char* getLine() {
  char* line = 0;
  size_t len = 0;
  getline(&line, &len, stdin);
  return line;
}
