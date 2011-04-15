package marisma.ssh.tunnel;

import java.io.IOException;
import java.io.InputStream;

class InputFacade extends InputStream {

    private volatile InputStream in;
    private volatile boolean changed=false;

    public void setIn(InputStream in) {
	this.changed=true;
	this.in = in;
    }
    public InputStream getIn() {
	return this.in;
    }
    public InputFacade(InputStream in) {
	this.in = in;
    }

    @Override
    public int read() throws IOException {
	int i = this.in.read();
	if (this.changed) {
	    this.changed=false;
	    i = this.in.read();
	}
	return i;
    }

    @Override
    public int read(byte[] b) throws IOException {
	int i = this.in.read(b);
	if (this.changed) {
	    this.changed=false;
	    i = this.in.read(b);
	}
	return i;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
	int i = this.in.read(b, off, len);
	if (this.changed) {
	    this.changed=false;
	    i = this.in.read(b,off,len);
	}
	return i;
    }

}
