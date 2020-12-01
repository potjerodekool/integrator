package com.github.potjerodekool.integrator.util

import java.io.OutputStream
import java.io.OutputStreamWriter

val DEF_NULL_OUTPUT_STREAM: OutputStream = DevNullOuputStream()
val DEF_NULL_OUTPUT_STREAM_WRITER: OutputStreamWriter = OutputStreamWriter(DEF_NULL_OUTPUT_STREAM)

fun OutputStreamWriter.writeLine(line: String): OutputStreamWriter {
    this.write(line)
    return this.newLine()
}

fun OutputStreamWriter.newLine(): OutputStreamWriter {
    this.write("\n")
    this.flush()
    return this
}

private class DevNullOuputStream : OutputStream() {
    override fun write(b: Int) {
        //Don't write anything.
    }
}



