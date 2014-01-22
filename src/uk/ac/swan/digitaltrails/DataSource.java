package uk.ac.swan.digitaltrails;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

//TODO: I dislike this class massively. HARD CODED WALKS AND POINTS. THIS IS USELESS.
//TODO: Rewrite so we can load only the walk names / ids and query for the waypoints when a walk is selected.
public class DataSource {

	private static ArrayList<Walk> allWalks;
	private static ArrayList<WalkPoint> allPoints;
	private static DataSource instance = null;
	private static Context mContext;

	protected DataSource() {
		// Exists only to defeat instantiation.
	}

	public static DataSource getInstance() {
		if (instance == null) {
			instance = new DataSource();
			allWalks = new ArrayList<Walk>();
			allPoints = new ArrayList<WalkPoint>();
			setupWalkList();
		}
		return instance;
	}

	public ArrayList<Walk> getWalks() {
		return allWalks;
	}

	public ArrayList<WalkPoint> getPointsForWalkID(int id) {
		Log.i("", "Getting points for walkID: " + id + " allWalks.size()="
				+ allWalks.size());

		Walk thisWalk = allWalks.get(id);
		ArrayList<Integer> thePointIDs = thisWalk.getPoints();
		ArrayList<WalkPoint> walkPoints = new ArrayList<WalkPoint>();
		for (Integer pointID : thePointIDs) {
			Log.i("", "Adding point to return list: " + pointID);

			walkPoints.add(allPoints.get(pointID.intValue()));

		}

		return walkPoints;
	}

	public WalkPoint getWalkPointID(int id) {
		Log.i("", "Getting walkpoint ID: " + id + " allPoints size: "
				+ allPoints.size());

		if (allPoints.size() > 0) {
			return allPoints.get(id);
		} else {
			return new WalkPoint();
		}

	}

	public static void setupWalkList() {

		// setup all walk points into data structure

		allPoints.add(new WalkPoint(0, 52.41630256175995, -4.065622687339783,
				"Astute Office", "This is the Astute office..."));
		allPoints.add(new WalkPoint(1, 52.416279, -4.066613, "IBERBach",
				"Small cafe."));
		allPoints.add(new WalkPoint(2, 52.416002, -4.06563, "Physics Main",
				"This is the physics building."));
		allPoints.add(new WalkPoint(3, 52.415713, -4.066304, "Biology Main",
				"This is the biology building."));
		allPoints.add(new WalkPoint(4, 52.416205, -4.065548,
				"Computer Science", "This is the computer science building."));
		allPoints.add(new WalkPoint(5, 52.416518, -4.066422, "Union Steps",
				"This is the steps to the union."));

		allPoints
				.add(new WalkPoint(
						6,
						52.2436,
						-4.26358,
						"General Storehouse",
						"Thought to be one of the oldest buildings along the Quay. In the C19th it stored goods brought in by ship and was later used as a mortuary. When the harbour was first built a plaque was erected to commemorate its completion. In time it disappeared but in 2007 a replica was erected and if you walk to the end of the pier you will see it on the lookout. To the north of the harbour entrance, the Reverend Alban Thomas Gwynne built a row of houses for the labourers employed in building the harbour, called Mynachdy Row, though locals named them Bedlam Barracks and in 1818 a school was built. All were lost to the sea in the late C19th.",
						new ArrayList<String>(Arrays.asList("image00"))));
		allPoints
				.add(new WalkPoint(
						7,
						52.2435,
						-4.26323,
						"Harbourmaster's House",
						"This building was the first building to be built along the new quay in about 1812. The height of the building made it easy to keep a lookout for smugglers. The ground floor was a public house and up until the 1950s it was known as the Red Lion.",
						new ArrayList<String>(Arrays.asList("image01"))));
		allPoints
				.add(new WalkPoint(
						8,
						52.2435,
						-4.26314,
						"Spillers Flour Warehouse",
						"Originally built as a house it was reportedly destroyed in a fire. A warehouse was erected in its place and in the early 1900s was owned by Spillers and used for the storage of flour transported by steam ships from the main outlet in Cardiff.",
						new ArrayList<String>(Arrays.asList("image02"))));
		allPoints
				.add(new WalkPoint(
						9,
						52.2434,
						-4.26301,
						"Steam Packet Company",
						"The Steam Navigation Company was established here in 1863 but went into liquidation when its ship the Prince Cadwgan, captained by John Evans, Milford House, was lost in 1876. The Aberayron Steam Packet Company was established in 1877 and ran steam ships on the milk run to Bristol. Payments were made at the harbourmaster’s office until December 1916. Steam ships continued to visit the harbour up until the 1920s. By 1819 there were five houses on Quay Parade; two were to become taverns – No 4, the Hope Inn, and No 10, the Swan Inn.",
						new ArrayList<String>(Arrays.asList("image03"))));

		allPoints.add(new WalkPoint(10, 52.416631, -4.065773,
				"Concourse carpark", "Car park near geog concourse."));
		allPoints
				.add(new WalkPoint(11, 52.416663, -4.066063,
						"Geog Concourse Entrance",
						"This is near geography concourse."));
		allPoints.add(new WalkPoint(12, 52.417776, -4.064748, "Penbryn",
				"This is near ta med ."));
		allPoints.add(new WalkPoint(13, 52.417089, -4.065135, "Junction", ""));
		allPoints.add(new WalkPoint(14, 52.416971, -4.065564, "Junction", ""));

		Walk walk1 = new Walk();
		walk1.setTitle("Aber Campus Longer Walk");
		walk1.setDesc("This is the first test walk in the static data.");

		ArrayList<Integer> pointSet1 = new ArrayList<Integer>();
		pointSet1.add(Integer.valueOf(4));
		pointSet1.add(Integer.valueOf(2));
		pointSet1.add(Integer.valueOf(5));
		pointSet1.add(Integer.valueOf(3));
		pointSet1.add(Integer.valueOf(1));
		walk1.setPoints(pointSet1);

		ArrayList<Integer> pointSet2 = new ArrayList<Integer>();
		pointSet2.add(Integer.valueOf(6));
		pointSet2.add(Integer.valueOf(7));
		pointSet2.add(Integer.valueOf(8));
		pointSet2.add(Integer.valueOf(9));

		Walk walk2 = new Walk();
		walk2.setTitle("Aberaeron Test Walk");
		walk2.setDesc("This is the second test walk in the static data.");
		walk2.setPoints(pointSet2);
		Walk walk3 = new Walk();
		walk3.setTitle("Aber Campus Short Walk");
		walk3.setDesc("This is the  test walk in the static data.");

		ArrayList<Integer> pointSet3 = new ArrayList<Integer>();
		pointSet3.add(Integer.valueOf(10));
		pointSet3.add(Integer.valueOf(14));
		pointSet3.add(Integer.valueOf(13));
		pointSet3.add(Integer.valueOf(12));

		walk3.setPoints(pointSet3);
		allWalks.add(walk1);
		allWalks.add(walk2);
		allWalks.add(walk3);
		Log.i("", "Added " + allWalks.size() + " to walk array.");

		// DatabaseHandler.getAllWalks();

	}

}
