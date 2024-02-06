/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.framework;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

class ProcessorTest {
    @Test
    void t1() {
        Processor processor = new Processor();
        Compilation compilation = Compiler.javac()
                .withProcessors(processor)
                .compile(JavaFileObjects.forResource("exampleProjects/correct/test/Main.java"));
        CompilationSubject.assertThat(compilation).succeeded();
    }
}
