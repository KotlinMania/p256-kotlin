import Testing
import P256

@Suite("P256 Export Smoke Tests")
struct P256ExportTests {
    @Test("Swift module loads cleanly")
    func testSwiftModuleLoads() throws {
        #expect(true)
    }
}
