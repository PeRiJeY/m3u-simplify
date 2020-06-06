package es.german.m3u.simplify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.iheartradio.m3u8.Encoding;
import com.iheartradio.m3u8.Format;
import com.iheartradio.m3u8.ParseException;
import com.iheartradio.m3u8.ParsingMode;
import com.iheartradio.m3u8.PlaylistException;
import com.iheartradio.m3u8.PlaylistParser;
import com.iheartradio.m3u8.PlaylistWriter;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.TrackData;
import com.iheartradio.m3u8.data.TrackInfo;

public class Simplify {

	private static final Pattern pattern = Pattern.compile("group-title=\"(.*)\"");
	
	File inputFile = null;
	Map<String, String> mapGroups = new HashMap<>();
	Playlist playlist = null;

	public Simplify(File inputFile) throws ParseException, PlaylistException, Exception {
		this.inputFile = inputFile;
	}

	public void run(List<String> checkedOptions) throws Exception, ParseException, PlaylistException {
		File outputFile = new File(outputFileName(inputFile.getAbsolutePath()));
		
		System.out.println("Output: " + outputFile.getAbsolutePath());

		List<TrackData> outputList = new ArrayList<>();

		if (playlist == null) {
			readInputFile();
		}

		if (playlist.hasMasterPlaylist() && playlist.getMasterPlaylist().hasUnknownTags()) {
			System.err.println(playlist.getMasterPlaylist().getUnknownTags());
		} else if (playlist.hasMediaPlaylist() && playlist.getMediaPlaylist().hasUnknownTags()) {
			System.err.println(playlist.getMediaPlaylist().getUnknownTags());
		} else {
			if (playlist.hasMasterPlaylist()) {
				System.out.println("Master!!!");
			} else if (playlist.hasMediaPlaylist()) {
				MediaPlaylist mediaPlaylist = playlist.getMediaPlaylist();
				List<TrackData> tracks = mediaPlaylist.getTracks();
				tracks.forEach(track -> {
					TrackInfo info = track.getTrackInfo();
					if (isInMyGroup(info, checkedOptions)) {
						outputList.add(track);
					}
				});
			}
		}

		MediaPlaylist outputMediaPlaylist = playlist.getMediaPlaylist().buildUpon().withTracks(outputList).build();

		Playlist outputPlaylist = playlist.buildUpon().withMediaPlaylist(outputMediaPlaylist).build();
		
		OutputStream outputStream = new FileOutputStream(outputFile);
		PlaylistWriter writer = new PlaylistWriter.Builder()
                .withOutputStream(outputStream)
                .withFormat(Format.EXT_M3U)
                .withEncoding(Encoding.UTF_8)
                .build();
		writer.write(outputPlaylist);
		outputStream.close();
	}
	
	private void readInputFile() throws Exception, ParseException, PlaylistException {
		InputStream inputStream = new FileInputStream(inputFile);
		
		System.out.println("Input: " + inputFile.getAbsolutePath());


		PlaylistParser parser = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
		playlist = parser.parse();

		if (playlist.hasMasterPlaylist() && playlist.getMasterPlaylist().hasUnknownTags()) {
			System.err.println(playlist.getMasterPlaylist().getUnknownTags());
		} else if (playlist.hasMediaPlaylist() && playlist.getMediaPlaylist().hasUnknownTags()) {
			System.err.println(playlist.getMediaPlaylist().getUnknownTags());
		} else {
			if (playlist.hasMasterPlaylist()) {
				System.out.println("Master!!!");
			} else if (playlist.hasMediaPlaylist()) {
				MediaPlaylist mediaPlaylist = playlist.getMediaPlaylist();
				List<TrackData> tracks = mediaPlaylist.getTracks();
				tracks.forEach(track -> {
					TrackInfo info = track.getTrackInfo();
					String group = info.attributes.get("group-title");
					mapGroups.put(group, group);
				});
			}
		}
		inputStream.close();

	}

	public boolean isInMyGroup(TrackInfo info, List<String> checkedOptions) {
		if (info.attributes != null) {
			String group = info.attributes.get("group-title");
			if (checkedOptions.contains(group)) {
				return true;
			}
		}
		return false;
	}
	
	public String outputFileName(String originalFileName) {
		String result = originalFileName.replaceAll("\\.m3u", "\\_output.m3u").replaceAll("\\.m3u8", "\\_output.m3u8");
		
		return result;
	}

	public List<String> readGroups() throws ParseException, PlaylistException, Exception {
		readInputFile();
		List<String> result = new ArrayList<>();
		for (String item : mapGroups.keySet()) {
			if (StringUtils.isNotEmpty(item)) {
				result.add(item);
			}
		}
		Collections.sort(result);
		
		return result;
	}

}
