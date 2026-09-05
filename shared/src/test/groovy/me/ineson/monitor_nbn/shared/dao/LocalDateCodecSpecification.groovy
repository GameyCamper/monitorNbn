package me.ineson.monitor_nbn.shared.dao;

import java.time.format.DateTimeParseException

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.EncoderContext

import spock.lang.*

class LocalDateCodecSpecification extends Specification {

	
	def "getEncoderClass"() {
		expect:
		new LocalDateCodec().encoderClass == LocalDate.class
	}

	def "encode LocalDate to ISO string null check"() {
		when:
		def codec = new LocalDateCodec()
		BsonWriter writer = Mock()
		codec.encode(writer, null, null)
		
		then:
		thrown(NullPointerException)
	 }

	@Unroll
	def "encode LocalDate to ISO string"(LocalDate input, String expectedResult) {
		
		given:
		def codec = new LocalDateCodec()
		BsonWriter writer = Mock()
	 
		when:
		def result = codec.encode(writer, input, null)
		 
		then:
        1 * writer.writeString(expectedResult)
 
		where:
		input                      | expectedResult
		 LocalDate.of(2023, 10, 5) | "2023-10-05"
		 LocalDate.MIN             | "-999999999-01-01"
		 LocalDate.MAX             | "+999999999-12-31"
	 }

	@Unroll
	def "decode ISO string to LocalDate tests"(String input, LocalDate expectedResult, Class<?> expectedException) {
        given:
        def codec = new LocalDateCodec()
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
		if (expectedResult) {
			assert resultException == null
			assert expectedResult == result
		} else {
			assert expectedException == resultException.class
		}

		where:
		input              | expectedResult              | expectedException
		"2023-10-05"       | LocalDate.of(2023, 10, 5)   | null
		"-0023-10-05"      | LocalDate.of(-23, 10, 5)    | null
		"-999999999-01-01" | LocalDate.MIN               | null
		"+999999999-12-31" | LocalDate.MAX               | null
		"2025-2-9"         | null                        | DateTimeParseException.class
		"-2025-2-9"        | null                        | DateTimeParseException.class
		"2023-13-32"       | null                        | DateTimeParseException.class
		"202332-01-02"     | null                        | DateTimeParseException.class
		"rubbish"          | null                        | DateTimeParseException.class
		null               | null                        | NullPointerException.class
    }

}

