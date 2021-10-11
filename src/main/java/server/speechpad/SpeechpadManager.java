package server.speechpad;

import server.realtime_transcribe.RealtimeTranscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpeechpadManager {

    private final Map<String, Speechpad> speechpadMap = new HashMap<>();

    public Speechpad create(String model, String name) {
        String speechpadId = UUID.randomUUID().toString();
        String speechpadName = (name == null) ? speechpadId : name;
        Speechpad speechpad = new Speechpad(speechpadId, speechpadName, new RealtimeTranscriber(model));
        synchronized (speechpadMap) {
            speechpadMap.put(speechpadId, speechpad);
        }
        return speechpad;
    }

    public List<Speechpad> getAllSpeechpad() {
        synchronized (speechpadMap) {
            return new ArrayList<>(speechpadMap.values());
        }
    }

    public Speechpad getSpeechpad(String speechpadId) throws NoSuchSpeechpadException {
        synchronized (speechpadMap) {
            return Optional.ofNullable(speechpadMap.get(speechpadId))
                .orElseThrow(() -> new NoSuchSpeechpadException(speechpadId));
        }
    }

    public boolean delete(String speechpadId) {
        synchronized (speechpadMap) {
            if (!speechpadMap.containsKey(speechpadId)) {
                return false;
            }
            speechpadMap.remove(speechpadId);
            return true;
        }
    }

    public void rename(String speechpadId, String newName) throws NoSuchSpeechpadException {
        synchronized (speechpadMap) {
            Optional.ofNullable(speechpadMap.get(speechpadId))
                .orElseThrow(() -> new NoSuchSpeechpadException(speechpadId))
                .setName(newName);
        }
    }

}
