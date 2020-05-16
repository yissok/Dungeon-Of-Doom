# Define a variable for classpath
# this is the .class files of you java code are in the directory above this one and in sub directory called bin
CLASS_PATH = ../

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : libMap.so

# $@ matches the target, $< matches the first dependancy
libMap.so : Map.o
	gcc -Wl -shared -o $@ $<

# $@ matches the target, $< matches the first dependancy
Map.o : Map.c Map.h
	gcc -fPIC -I /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.38.x86_64/include -I /usr/lib/jvm/java-1.6.0-openjdk-1.6.0.38.x86_64/include/linux -c $< -o $@

# $* matches the target filename without the extension
Map.h : Map.class
	javah -classpath $(CLASS_PATH) $*

clean :
	rm Map.h Map.o libMap.so
