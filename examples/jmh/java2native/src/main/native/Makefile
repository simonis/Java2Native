SRC_FILES = $(shell find $(SRC_DIR) -name '*.cpp')

libNativeCall.so: $(SRC_FILES)
	g++ -m64 -fPIC -g -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux -I ${GENERATED_JAVAH} -shared -o $@ $^
