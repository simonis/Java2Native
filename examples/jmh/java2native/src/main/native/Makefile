SRC_FILES = $(shell find $(SRC_DIR) -name '*.cpp')

libNativeCall.so: $(SRC_FILES)
	g++ -fPIC -I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux \
                  -I $(JAVA_HOME)/../include -I $(JAVA_HOME)/../include/linux \
                  -I ${GENERATED_JAVAH} -shared -o $@ $^
