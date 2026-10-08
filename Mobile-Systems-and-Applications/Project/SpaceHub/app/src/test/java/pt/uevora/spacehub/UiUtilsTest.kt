package pt.uevora.spacehub

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.uevora.spacehub.ui.util.isValidIsoDate

class UiUtilsTest {

    /**
     * Tests the ISO Date Validation util function.
     */
    @Test
    fun isValidIsoDate_validAndInvalidDates() {
        // Valid Dates
        assertTrue("2026-05-23".isValidIsoDate())
        assertTrue("2026-06-15".isValidIsoDate())

        // Invalid Dates
        assertFalse("23-05-2026".isValidIsoDate())
        assertFalse("2026-99-99".isValidIsoDate())
    }
}