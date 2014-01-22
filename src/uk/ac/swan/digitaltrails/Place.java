/**
 * @author Lewis Hancock
 * @version %I% %G%
 *
 */
package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

public class Place {

	private int id;
	private String title;
	private String shortDescription;
	private String longDescription;
	private double longitude;
	private double latitude;
	private ArrayList<Photo> photos;
	private ArrayList<Audio> audioFiles;
	private ArrayList<Video> videos;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	public ArrayList<Audio> getAudioFiles() {
		return audioFiles;
	}

	public void setAudioFiles(ArrayList<Audio> audioFiles) {
		this.audioFiles = audioFiles;
	}

	public ArrayList<Video> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<Video> videos) {
		this.videos = videos;
	}

	public Place() {
		setID(0);
		setTitle("New Place");
		setShortDescription("short description");
		setLongDescription("long description");
		setLatitude(0);
		setLongitude(0);
	}

	public Place(int id, String title, String shortDescription,
			String longDescription, double longitude, double latitude,
			ArrayList<Photo> photos, ArrayList<Audio> audioFiles,
			ArrayList<Video> videos) {
		setID(0);
		setTitle("New Place");
		setShortDescription("short description");
		setLongDescription("long description");
		setLatitude(0);
		setLongitude(0);
		setPhotos(photos);
		setAudioFiles(audioFiles);
		setVideos(videos);
	}

}
