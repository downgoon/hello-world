package hello;

public class Greeting {

    private final long times;
    private final String content;

    public Greeting(long times, String content) {
        this.times = times;
        this.content = content;
    }

    public long getTimes() {
        return times;
    }

    public String getContent() {
        return content;
    }
}
