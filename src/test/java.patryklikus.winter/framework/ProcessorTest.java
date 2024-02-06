package com.patryklikus.winter.framework;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import com.patryklikus.winter.framework.Processor;

public class ProcessorTest {
    @Test
    void t1() {
        Processor processor = new Processor();
        Compilation compilation = Compiler.javac()
                .withProcessors(processor)
                .compile(JavaFileObjects.forResource("exampleProjects/correct/test/Main.java"));
        CompilationSubject.assertThat(compilation).succeeded();
    }
}
