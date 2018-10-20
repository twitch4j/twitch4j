+++
title="Stream"
weight = 100
+++

# Stream Model

Get Streams

Gets information about active streams. Streams are returned sorted by number of current viewers, in descending order. Across multiple pages of results, there may be duplicate or missing streams, as viewers join and leave streams.

The response has a JSON payload with a data field containing an array of stream information elements and a pagination field containing information required to query for more streams.