package com.microsoft.azure.cosmosdb.kafka.connect.processor

import com.google.gson._
import org.apache.kafka.connect.sink.SinkRecord
import org.apache.kafka.connect.source.SourceRecord

abstract class JsonPostProcessor extends PostProcessor {

  override final def runPostProcess(sourceRecord: SourceRecord): SourceRecord = {
    val jsonParser = new JsonParser()
    val json: JsonObject = jsonParser.parse(sourceRecord.value().toString).getAsJsonObject()

    val processedJson = runJsonPostProcess(json)

    val result = new SourceRecord(
      sourceRecord.sourcePartition,
      sourceRecord.sourceOffset,
      sourceRecord.topic,
      null,
      processedJson.toString
    )

    result
  }

  override def runPostProcess(sinkRecord: SinkRecord): SinkRecord = {
    val jsonParser = new JsonParser()
    val json: JsonObject = jsonParser.parse(sinkRecord.value().toString).getAsJsonObject()

    val processedJson = runJsonPostProcess(json)

    val result = new SinkRecord(
      sinkRecord.kafkaPartition(),
      sinkRecord.kafkaOffset(),
      sinkRecord.topic,
      null,
      processedJson.toString
    )

    result
  }

  def runJsonPostProcess(json: JsonObject): JsonObject
}
