package marisma.ssh.tunnel;

import java.io.IOException;
import java.io.OutputStream;

class OutputFacade extends OutputStream {

    private volatile OutputStream out;

    public void setOut(OutputStream out) {
	this.out = out;
    }

    public OutputStream getOut() {
	return this.out;
    }
    public OutputFacade(OutputStream out) {
	this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
	this.out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
	this.out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
	this.out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
	this.out.flush();
    }

}
