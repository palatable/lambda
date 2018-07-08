package testsupport.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class InstantRecordingClock extends Clock {
    private final Clock         clock;
    private final List<Instant> instants;
    private       Instant       lastInstant;

    public InstantRecordingClock(Clock clock) {
        this.clock = clock;
        instants = new ArrayList<>();
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new InstantRecordingClock(clock.withZone(zone));
    }

    @Override
    public Instant instant() {
        return lastInstant = clock.instant();
    }

    public Instant saveLastInstant() {
        instants.add(lastInstant);
        return lastInstant;
    }

    public List<Instant> instants() {
        return instants;
    }
}
