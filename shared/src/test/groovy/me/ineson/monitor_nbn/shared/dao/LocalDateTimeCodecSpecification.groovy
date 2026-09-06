package me.ineson.monitor_nbn.shared.dao;

import java.time.format.DateTimeParseException

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.EncoderContext

import spock.lang.*

class LocalDateTimeCodecSpecification extends Specification {

	
	def "getEncoderClass"() {
		expect:
            new LocalDateTimeCodec().encoderClass == LocalDateTime.class
	}

	def "encode LocalDateTime to ISO string null check"() {
		when:
            def codec = new LocalDateTimeCodec()
            BsonWriter writer = Mock()
            codec.encode(writer, null, null)
		
		then:
            thrown(NullPointerException)
	 }

	@Unroll
	def "encode LocalDateTime to ISO string"(LocalDateTime input, String expectedResult) {
		
		given:
            def codec = new LocalDateTimeCodec()
            BsonWriter writer = Mock()
	 
		when:
            def result = codec.encode(writer, input, null)
		 
		then:
            1 * writer.writeString(expectedResult)
 
		where:
            input                                      | expectedResult
            LocalDateTime.of(2023, 10, 5, 1, 2)       | "2023-10-05T01:02"
            LocalDateTime.of(2022, 9, 25, 19, 2, 44)  | "2022-09-25T19:02:44"
            LocalDateTime.MIN                         | "-999999999-01-01T00:00"
            LocalDateTime.MAX                         | "+999999999-12-31T23:59:59.999999999"
	 }

	@Unroll
	def "decode ISO string to LocalDateTime tests"(String input, LocalDateTime expectedResult, Class<?> expectedException) {
        given:
            def codec = new LocalDateTimeCodec()
            def reader = Stub(BsonReader) {
                readString() >> input
            }

        when:
        def result = null
        def resultException = null
		try {
		     result = codec.decode(reader, null)
		} catch (e) {
			resultException = e
		}
		
		then:
            if( expectedResult) {
                assert resultException == null
                assert expectedResult == result
            } else {
                assert expectedException == resultException.class
            }

		where:
            input                                 | expectedResult                            | expectedException
            "2023-10-05T10:03:13"                 | LocalDateTime.of(2023, 10, 5, 10 , 3, 13) | null
            "-0023-10-05T21:57"                   | LocalDateTime.of(-23, 10, 5, 21, 57)      | null
            "-999999999-01-01T00:00:00.0"         | LocalDateTime.MIN                         | null
            "+999999999-12-31T23:59:59.999999999" | LocalDateTime.MAX                         | null
            "2025-2-9T2:2"                        | null                                      | DateTimeParseException.class
            "-2025-2-9"                           | null                                      | DateTimeParseException.class
            "2023-13-32T00:00"                    | null                                      | DateTimeParseException.class
            "2023-11-11T0:0"                      | null                                      | DateTimeParseException.class
            "2023-11-11T01"                       | null                                      | DateTimeParseException.class
            "rubbish"                             | null                                      | DateTimeParseException.class
            null                                  | null                                      | NullPointerException.class
    }

}

