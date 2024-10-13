package edu.grinnell.csc207.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class implements tests for the MatrixV0 class.
 */
public class MatrixV0Tests {
  /**
   * This test checks the functionality of MatrixV0.width() and MatrixV0.height()
   * in conjunction with MatrixV0.insertRow() and MatrixV0.insertCol().
   */
  @Test
  public void stroudDavidTestWidthHeight() {
    int width = 5;
    int height = 4;

    MatrixV0<String> mstr = new MatrixV0<>(width, height, "DEFAULT");

    assertEquals(width, mstr.width(), "Newly instantiated matrix should have width " + width);
    assertEquals(height, mstr.height(), "Newly instantiated matrix should have height " + height);

    mstr.insertCol(2);
    try {
      mstr.insertCol(6, new String[height]);
    } catch (ArraySizeException err) {
      fail("insertCol threw ArraySizeException with appropriately sized array");
    } // try-catch

    assertEquals(
            width + 2,
            mstr.width(),
            "Matrix of width "
                    + width
                    + " with 2 added columns should have width "
                    + (width + 2)
    );

    mstr.deleteCol(0);

    assertEquals(
            width + 1,
            mstr.width(),
            "Matrix of width "
                    + width
                    + " with 2 added columns and one removed column should have width "
                    + (width + 1)
    );

    for (int i = 1; i <= height; i++) {
      mstr.deleteRow(height - i);
      assertEquals(
              height - i,
              mstr.height(),
              "Matrix of height "
                      + height
                      + " with "
                      + i
                      + " removed columns should have height "
                      + (height - i)
      );
    } // for

    mstr.insertRow(0);
    assertEquals(
            1,
            mstr.height(),
            "Matrix of height "
                    + height
                    + " with "
                    + height
                    + " elements removed and 1 added should have height 1"
    );
  } // stroudDavidTestWidthHeight()

  /**
   * This test checks the functionality of MatrixV0.get() and MatrixV0.set().
   */
  @Test
  public void stroudDavidTestGetSet() {
    int width = 5;
    int height = 4;
    String defaultValue = "DEFAULT";
    String newValue = "NEW VALUE";
    String newestValue = "NEWEST VALUE";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        assertEquals(
                defaultValue,
                mstr.get(i, j),
                "Newly instantiated matrix should have default value everywhere"
        );
      } // for
    } // for

    mstr.set(1, 2, newValue);
    assertEquals(
            newValue,
            mstr.get(1, 2),
            "Value just set should be available via get"
    );
    assertEquals(
            defaultValue,
            mstr.get(2, 1),
            "Value just set should not show up in transposed position"
    );

    mstr.set(1, 2, newestValue);
    assertEquals(
            newestValue,
            mstr.get(1, 2),
            "Value should be overwriteable via set"
    );
    assertEquals(
            defaultValue,
            mstr.get(2, 1),
            "Overwritten value should not show up in transposed position"
    );

    mstr.set(1, 2, null);
    assertNull(
            mstr.get(1, 2),
            "Null should be available via get"
    );
    assertEquals(
            defaultValue,
            mstr.get(2, 1),
            "Null should not show up in transposed position"
    );
  } // stroudDavidTestGetSet()

  /**
   * This checks the functionality of MatrixV0.get() and MatrixV0.set()
   * in conjunction with subtype-polymorphic values.
   */
  @Test
  public void stroudDavidTestPolymorphicGetSet() {
    int width = 3;
    int height = 4;

    MatrixV0<Object> mobj = new MatrixV0<>(width, height);

    class Polymorphic {
      final int val;

      public Polymorphic(int val1) {
        this.val = val1;
      } // Polymorphic(int)

      public int get() {
        return this.val;
      } // get()
    } // class Polymorphic

    Object polymorphicValue = new Polymorphic(5);
    mobj.set(1, 1, polymorphicValue);

    try {
      Object receivedObject = mobj.get(1, 1);
      Polymorphic receivedPolymorphic = (Polymorphic) receivedObject;
      assertEquals(
              5,
              receivedPolymorphic.get(),
              "Receiving polymorphic objects from matrices "
                      + "should result in no changes to the polymorphic object"
      );
    } catch (ClassCastException ex) {
      fail("ClassCastException should not be thrown when getting polymorphic value from matrix");
    } // try-catch
  } // stroudDavidTestPolymorphicGetSet()

  /**
   * This test checks the functionality of MatrixV0.insertRow().
   */
  @Test
  public void stroudDavidTestInsertRow() {
    int width = 3;
    int height = 4;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.set(2, 1, markerValue);

    mstr.insertRow(4);
    for (int i = 0; i < width; i++) {
      assertEquals(
              defaultValue,
              mstr.get(4, i),
              "Inserted row, column "
              + i
              + " should have default value if no value was specified"
      );
    } // for

    String[] insertedRow = new String[width];
    // This will leave the last value in the array as null.
    for (int i = 0; i < width - 1; i++) {
      insertedRow[i] = Integer.valueOf(i).toString();
    } // for

    try {
      mstr.insertRow(1, insertedRow);
    } catch (ArraySizeException ex) {
      fail("insertRow should not throw ArraySizeException "
              + "for an appropriately sized array");
    } // try-catch

    for (int i = 0; i < width; i++) {
      assertEquals(
              insertedRow[i],
              mstr.get(1, i),
              "Column "
              + i
              + " of inserted row 1 should have value given "
              + "in insertRow"
      );
    } // for

    assertEquals(
            markerValue,
            mstr.get(3, 1),
            "Marker value should be pushed forward when inserting rows"
    );
  } // stroudDavidTestInsertRow()

  /**
   * This test checks the functionality of MatrixV0.insertCol().
   */
  @Test
  public void stroudDavidTestInsertCol() {
    int width = 4;
    int height = 3;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.set(1, 2, markerValue);

    mstr.insertCol(4);
    for (int i = 0; i < height; i++) {
      assertEquals(
              defaultValue,
              mstr.get(i, 4),
              "Inserted column, row "
                      + i
                      + " should have default value if no value was specified"
      );
    } // for

    String[] insertedCol = new String[height];
    // This will leave the last value in the array as null.
    for (int i = 0; i < height - 1; i++) {
      insertedCol[i] = Integer.valueOf(i).toString();
    } // for

    try {
      mstr.insertCol(1, insertedCol);
    } catch (ArraySizeException ex) {
      fail("insertCol should not throw ArraySizeException "
              + "for an appropriately sized array");
    } // try-catch

    for (int i = 0; i < height; i++) {
      assertEquals(
              insertedCol[i],
              mstr.get(i, 1),
              "Column "
                      + i
                      + " of inserted row 1 should have value given "
                      + "in insertRow"
      );
    } // for

    assertEquals(
            markerValue,
            mstr.get(1, 3),
            "Marker value should be pushed forward when inserting columns"
    );
  } // stroudDavidTestInsertRow()

  /**
   * This internal function is used by stroudDavid tests to check that
   * inserted rows and columns are filled with null when null is
   * the default value.
   * @param matrix The matrix to test.
   * @param width The initial width of the matrix.
   * @param height The initial height of the matrix.
   */
  private void stroudDavidNullDefaultInsert(Matrix<String> matrix, int width, int height) {
    matrix.insertCol(3);
    matrix.insertRow(3);

    for (int i = 0; i < width + 1; i++) {
      assertNull(
              matrix.get(3, i),
              "Getting inserted row, column "
                      + i
                      + " should be null if no default value was specified"
      );
    } // for
    for (int i = 0; i < height + 1; i++) {
      assertNull(
              matrix.get(i, 3),
              "Getting inserted column, row "
                      + i
                      + " should be null if no default value was specified"
      );
    } // for
  } // stroudDavidTestNullDefaultInsert(Matrix<String>, int, int)

  /**
   * This test checks the functionality of MatrixV0.insertRow() and
   * MatrixV0.insertCol() when the default value is explicitly set to null.
   */
  @Test
  public void stroudDavidTestNullDefaultInsertSpecified() {
    stroudDavidNullDefaultInsert(new MatrixV0<String>(4, 3, null), 4, 3);
  } // stroudDavidTestNullDefaultInsert()

  /**
   * This test checks the functionality of MatrixV0.insertRow() and
   * MatrixV0.insertCol() when the default value is implicitly set to null.
   */
  @Test
  public void stroudDavidTestNullDefaultInsertUnspecified() {
    stroudDavidNullDefaultInsert(new MatrixV0<String>(4, 3), 4, 3);
  } // stroudDavidTestNullDefaultInsertUnspecified()

  /**
   * This test checks the functionality of MatrixV0.deleteRow() and
   * MatrixV0.deleteCol().
   */
  @Test
  public void stroudDavidTestDelete() {
    int width = 4;
    int height = 5;
    String markerValue = "MARKER";

    MatrixV0<String> mstr = new MatrixV0<>(width, height);
    mstr.set(2, 1, markerValue);

    mstr.deleteRow(1);
    assertEquals(
            markerValue,
            mstr.get(1, 1),
            "Marker value should be moved when deleting row before it"
    );

    mstr.deleteCol(0);
    assertEquals(
            markerValue,
            mstr.get(1, 0),
            "Marker value should be moved when deleting column before it"
    );

    mstr.deleteRow(2);
    assertEquals(
            markerValue,
            mstr.get(1, 0),
            "Marker value should not be moved when deleting row after it"
    );

    mstr.deleteCol(1);
    assertEquals(
            markerValue,
            mstr.get(1, 0),
            "Marker value should not be moved when deleting column after it"
    );
  } // stroudDavidTestDeleteRow()

  /**
   * This test checks the functionality of MatrixV0.fillRegion().
   */
  @Test
  public void stroudDavidTestFillRegion() {
    int width = 6;
    int height = 5;
    String defaultValue = "DEFAULT";
    String fillValue = "FILLED";
    String keepValue = "KEEP ME";

    MatrixV0<String> mstr = new MatrixV0<String>(width, height, defaultValue);
    mstr.set(1, 1, "OVERWRITE ME");
    mstr.set(4, 4, keepValue);

    mstr.fillRegion(1, 0, 3, 2, fillValue);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        String expected = defaultValue;
        if (1 <= i && i < 3 && j < 2) {
          expected = fillValue;
        } // if
        if (i == 4 && j == 4) {
          expected = keepValue;
        } // if

        assertEquals(
                expected,
                mstr.get(i, j),
                "After filling, row "
                + i
                + ", column "
                + j
                + " should be "
                + expected
        );
      } // for
    } // for

    mstr.fillRegion(2, 2, 3, 3, null);
    assertNull(
            mstr.get(2, 2),
            "Filling a region with null should give null"
    );
  } // stroudDavidTestFillRegion()

  /**
   * This test checks the functionality of MatrixV0.fillLine().
   */
  @Test
  public void stroudDavidTestFillLine() {
    int width = 6;
    int height = 5;
    String defaultValue = "DEFAULT";
    String fillValue = "FILLED";
    String keepValue = "KEEP ME";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.set(3, 4, keepValue);

    mstr.fillLine(1, 1, 1, 2, height, width, fillValue);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        String expected = defaultValue;
        if ((i == 1 && j == 1)
                || (i == 2 && j == 3)
                || (i == 3 && j == 5)) {
          expected = fillValue;
        } // if
        if (i == 3 && j == 4) {
          expected = keepValue;
        } // if

        assertEquals(
                expected,
                mstr.get(i, j),
                "Value expected at row "
                + i
                + ", column "
                + j
                + " after filling a line is "
                + expected
        );
      } // for
    } // for

    mstr.deleteCol(5);
    // Here, we "leap" over the end row and end column, and use null as a fill value.
    mstr.fillLine(4, 4, 2, 2, 5, 5, null);
    assertNull(
            mstr.get(4, 4),
            "Line starting at row 4, column 4 should have filled with null"
    );
  } // stroudDavidTestFillLine()

  /**
   * This test checks the functionality of MatrixV0.clone().
   */
  @Test
  public void stroudDavidTestClone() {
    int width = 4;
    int height = 4;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";
    String garbageValue = "GARBAGE";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.set(1, 1, markerValue);

    Matrix<String> mclone = mstr.clone();

    mstr.fillRegion(0, 0, 3, 2, garbageValue);
    mstr.fillLine(1, 1, 1, 1, 3, 3, garbageValue);
    mstr.set(1, 1, null);
    mstr.insertRow(3);

    mclone.set(1, 2, null);
    assertEquals(
            defaultValue,
            mstr.get(1, 2),
            "Changing clone should not affect original"
    );

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        String expected = defaultValue;
        if (i == 1 && j == 1) {
          expected = markerValue;
        } // if
        if (i == 1 && j == 2) {
          expected = null;
        } // if

        assertEquals(
                expected,
                mclone.get(i, j),
                "Expected "
                + expected
                + " at row "
                + i
                + ", column "
                + j
                + " of clone"
        );
      } // for
    } // for

    mclone.insertCol(2);
    for (int i = 0; i < height; i++) {
      assertEquals(
              defaultValue,
              mclone.get(i, 2),
              "Inserted column, row "
              + i
              + " in clone should have default value of original"
      );
    } // for
  } // stroudDavidTestClone()

  /**
   * This test checks the functionality of MatrixV0.equals().
   */
  @Test
  public void stroudDavidTestEquals() {
    int width = 3;
    int height = 3;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";
    String markerValueEquivalent = markerValue.toUpperCase();

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.set(1, 0, markerValue);
    assertTrue(
            mstr.equals(mstr),
            "Matrix should be equal to itself"
    );
    Matrix<String> mclone = mstr.clone();
    assertTrue(
            mstr.equals(mclone),
            "Matrix should be equal to its clone"
    );
    assertTrue(
            mclone.equals(mstr),
            "Clone should be equal to its original"
    );
    mclone.set(1, 1, markerValue);
    assertFalse(
            mclone.equals(mstr),
            "Clone, once modified, should not be equal to its original"
    );
    mstr.fillRegion(1, 1, 2, 2, markerValueEquivalent);
    assertTrue(
            mclone.equals(mstr),
            "Clone and original, once modified in the same way, should be equal"
    );
    mclone.insertCol(width);
    assertFalse(
            mstr.equals(mclone),
            "Matrix should not be equal to its clone "
                    + "if the clone has been extended"
    );
    mclone.deleteCol(width);
    mstr.set(0, 0, null);
    mclone.fillLine(0, 0, 1, 1, 1, 1,null);
    assertTrue(
            mstr.equals(mclone),
            "Equals should handle nulls"
    );
  } // stroudDavidTestEquals()

  /**
   * This interface is used by methods internal to stroudDavid to define
   * a function that can throw and takes in two indices as input.
   */
  private interface StroudDavidThrowableIndexFunction {
    void execute(int i, int j) throws Exception;
  } // interface StroudDavidThrowableIndexFunction

  /**
   * This method, used internally by stroudDavid tests, runs a function and
   * expects it to throw on a number of invalid indices, given a width
   * and height.
   * @param width The width of the matrix being tested.
   * @param height The height of the matrix being tested.
   * @param func The function that should throw when given an invalid input.
   */
  private void stroudDavidBounds(int width, int height, StroudDavidThrowableIndexFunction func) {
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(height, width - 1),
            "Using an invalid row should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(width - 1, height),
            "Using an invalid column should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(width + 2, height + 2),
            "Using an invalid row and column should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(-1, 0),
            "Using a negative row should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(0, -1),
            "Using a negative column should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(-2, -4),
            "Using a negative row and column should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(height + 2, -1),
            "Using an invalid row and a negative column "
                    + "should throw"
    );
    assertThrows(
            IndexOutOfBoundsException.class,
            () -> func.execute(-1, width + 2),
            "Using an invalid column and a negative row "
                    + "should throw"
    );
  } // stroudDavidBounds(int, int, StroudDavidThrowableIndexFunction)

  /**
   * This test checks whether MatrixV0.set() checks bounds properly.
   */
  @Test
  public void stroudDavidTestSetBounds() {
    int width = 7;
    int height = 4;

    MatrixV0<String> mstr = new MatrixV0<>(width, height);
    stroudDavidBounds(width, height, (i, j) -> mstr.set(i, j, "Hello!"));
  } // stroudDavidTestSetBounds()

  /**
   * This test chercks whether MatrixV0.get() checks bounds properly.
   */
  @Test
  public void stroudDavidTestGetBounds() {
    int width = 7;
    int height = 4;

    MatrixV0<String> mstr = new MatrixV0<>(width, height);
    stroudDavidBounds(width, height, mstr::get);
  } // stroudDavidTestGetBounds()

  /**
   * This test checks whether MatrixV0.fillRegion() checks bounds properly.
   */
  @Test
  public void stroudDavidTestFillRegionBounds() {
    int width = 7;
    int height = 4;

    MatrixV0<String> mstr = new MatrixV0<>(width, height);
    stroudDavidBounds(width, height, (i, j) -> mstr.fillRegion(
            i,
            j,
            width,
            height,
            "Hello!"
    ));
    stroudDavidBounds(width, height, (i, j) -> mstr.fillRegion(
            0,
            0,
            i >= 0 ? i + 1 : i,
            j >= 0 ? j + 1 : j,
            "Hello!"
    ));
  } // stroudDavidTestFillRegionBounds()

  /**
   * This test checks whether MatrixV0.fillLine() checks bounds properly.
   */
  @Test
  public void stroudDavidTestFillLineBounds() {
    int width = 7;
    int height = 4;

    MatrixV0<String> mstr = new MatrixV0<>(width, height);
    stroudDavidBounds(width, height, (i, j) -> mstr.fillLine(
            i,
            j,
            1,
            1,
            width,
            height,
            "Hello!"
    ));
    stroudDavidBounds(width, height, (i, j) -> mstr.fillLine(
            0,
            0,
            1,
            1,
            i >= 0 ? i + 1 : i,
            j >= 0 ? j + 1 : j,
            "Hello!"
    ));

    assertThrows(
            Exception.class,
            () -> mstr.fillLine(
                    1,
                    0,
                    1,
                    1,
                    0,
                    1,
                    "Hello!"
            ),
            "fillLine should throw when given endRow before startRow "
            + "(deltaRow is positive)"
    );

    assertThrows(
            Exception.class,
            () -> mstr.fillLine(
                    0,
                    1,
                    1,
                    1,
                    1,
                    0,
                    "Hello!"
            ),
            "fillLine should throw when given endCol before startCol "
            + "(deltaCol is positive)"
    );

    assertThrows(
            Exception.class,
            () -> mstr.fillLine(
                    2,
                    2,
                    -1,
                    -1,
                    3,
                    0,
                    "Hello!"
            ),
            "fillLine should throw when given startRow before endRow "
                    + "(deltaRow is negative)"
    );

    assertThrows(
            Exception.class,
            () -> mstr.fillLine(
                    2,
                    2,
                    -1,
                    -1,
                    0,
                    3,
                    "Hello!"
            ),
            "fillLine should throw when given startCol before endCol "
                    + "(deltaCol is negative)"
    );
  } // stroudDavidTestFillLineBounds()

  /**
   * This test checks whether MatrixV0.fillLine() can handle
   * lines specified "backwards," i.e. with
   * deltaRow and deltaCol both negative.
   */
  @Test
  public void stroudDavidTestFillLineBackwards() {
    int width = 3;
    int height = 3;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.fillLine(
            2,
            1,
            -1,
            -1,
            -1,
            -1,
            markerValue
    );
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        String expected = defaultValue;
        if ((i == 2 && j == 1)
                || (i == 1 && j == 0)) {
          expected = markerValue;
        } // if
        assertEquals(
                expected,
                mstr.get(i, j),
                "Value at row "
                + i
                + ", column "
                + j
                + " should be "
                + expected
        );
      } // for
    } // for
  } // stroudDavidTestFillLineBackwards()

  /**
   * This test checks whether MatrixV0.fillLine() can
   * handle purely vertical lines.
   */
  @Test
  public void stroudDavidTestFillLineVertical() {
    int width = 3;
    int height = 3;
    String defaultValue = "DEFAULT";
    String markerValue = "MARKER";

    MatrixV0<String> mstr = new MatrixV0<>(width, height, defaultValue);
    mstr.fillLine(
            2,
            1,
            -1,
            0,
            -1,
            -1,
            markerValue
    );
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        String expected = defaultValue;
        if ((i == 2 && j == 1)
                || (i == 1 && j == 1)
                || (i == 0 && j == 1)) {
          expected = markerValue;
        } // if
        assertEquals(
                expected,
                mstr.get(i, j),
                "Value at row "
                        + i
                        + ", column "
                        + j
                        + " should be "
                        + expected
        );
      } // for
    } // for
  } // stroudDavidTestFillLineVertical()

  /**
   * This method, used internally by stroudDavid tests, calls a function
   * with a variety of invalid inputs, expecting the function to throw
   * in all cases.
   * @param func The function being tested.
   */
  private void stroudDavidBoundsConstructor(StroudDavidThrowableIndexFunction func) {
    assertThrows(
            NegativeArraySizeException.class,
            () -> func.execute(-1, 2),
            "Creating a matrix with negative width should throw"
    );
    assertThrows(
            NegativeArraySizeException.class,
            () -> func.execute(2, -1),
            "Creating a matrix with negative height should throw"
    );
    assertThrows(
            NegativeArraySizeException.class,
            () -> func.execute(-3, -2),
            "Create a matrix with a negative width and height should throw"
    );
  } // stroudDavidBoundsConstructor(StroudDavidThrowableIndexFunction)

  /**
   * This test checks whether the constructor of MatrixV0 checks bounds properly.
   */
  @Test
  public void stroudDavidTestBoundsConstructor() {
    stroudDavidBoundsConstructor(MatrixV0::new);
    stroudDavidBoundsConstructor((i, j) -> new MatrixV0<String>(i, j, "Hello!"));
  } // stroudDavidTestBoundsConstructor()
} // class MatrixV0Tests
