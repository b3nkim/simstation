package mvc;

public enum Heading {
	NORTH, EAST, SOUTH, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;

	public static Heading random() {
		return Heading.values()[Utilities.rng.nextInt(8)];
	}
}