package me.philippheuer.twitch4j.tmi.chat;

import com.jcabi.log.Logger;
import lombok.Getter;
import lombok.Setter;

import com.neovisionaries.ws.client.*;

import me.philippheuer.twitch4j.TwitchClient;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Getter
@Setter
public class WsIrcListener implements WebSocketListener {
	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	WsIrcListener(TwitchClient twitchClient) { setTwitchClient(twitchClient); }

	/**
	 * Called after the state of the WebSocket changed.
	 *
	 * @param websocket The WebSocket.
	 * @param newState  The new state of the WebSocket.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 1.1
	 */
	@Override
	public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

	}

	/**
	 * Called after the opening handshake of the WebSocket connection succeeded.
	 *
	 * @param websocket The WebSsocket.
	 * @param headers   HTTP headers received from the server. Keys of the map are
	 *                  HTTP header names such as {@code "Sec-WebSocket-Accept"}.
	 *                  Note that the comparator used by the map is {@link
	 *                  String#CASE_INSENSITIVE_ORDER}.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
		WsIrcParser.initialize(websocket, getTwitchClient());
	}

	/**
	 * Called when {@link WebSocket#connectAsynchronously()} failed.
	 * <p>
	 * <p>
	 * Note that this method is called only when {@code connectAsynchronously()}
	 * was used and the {@link WebSocket#connect() connect()} executed in the
	 * background thread failed. Neither direct synchronous {@code connect()}
	 * nor {@link WebSocket#connect(ExecutorService)
	 * connect(ExecutorService)} will trigger this callback method.
	 * </p>
	 *
	 * @param websocket The WebSocket.
	 * @param cause     The exception thrown by {@link WebSocket#connect() connect()}
	 *                  method.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 1.8
	 */
	@Override
	public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

	}

	/**
	 * Called after the WebSocket connection was closed.
	 *
	 * @param websocket        The WebSocket.
	 * @param serverCloseFrame The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 *                         >close frame</a> which the server sent to this client.
	 *                         This may be {@code null}.
	 * @param clientCloseFrame The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 *                         >close frame</a> which this client sent to the server.
	 *                         This may be {@code null}.
	 * @param closedByServer   {@code true} if the closing handshake was started by the server.
	 *                         {@code false} if the closing handshake was started by the client.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

	}

	/**
	 * Called when a frame was received. This method is called before
	 * an <code>on<i>Xxx</i>Frame</code> method is called.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a continuation frame (opcode = 0x0) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The continuation frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a text frame (opcode = 0x1) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The text frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a binary frame (opcode = 0x2) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The binary frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 * >close frame</a> (opcode = 0x8) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1">close frame</a>.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a <a href="https://tools.ietf.org/html/rfc6455#section-5.5.2"
	 * >ping frame</a> (opcode = 0x9) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.2">ping frame</a>.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a <a href="https://tools.ietf.org/html/rfc6455#section-5.5.3"
	 * >pong frame</a> (opcode = 0xA) was received.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The <a href="https://tools.ietf.org/html/rfc6455#section-5.5.3">pong frame</a>.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a text message was received.
	 *
	 * @param websocket The WebSocket.
	 * @param text      The text message.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	public void onTextMessage(WebSocket websocket, String text) throws Exception {
		if (text.startsWith("PING")) {
			websocket.sendText("PONG :tmi.twitch.tv");
			Logger.debug(this, "Responded to PING from Twitch IRC.");
		} else if (text.startsWith(":tmi.twitch.tv PONG")) {
			Logger.debug(this, "Responded to PONG from Twitch IRC.");
		} else {
			WsIrcParser ircParser = new WsIrcParser(getTwitchClient(), websocket, text);
			// TODO: required listener for parsed data
		}
	}

	/**
	 * Called when a binary message was received.
	 *
	 * @param websocket The WebSocket.
	 * @param binary    The binary message.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

	}

	/**
	 * Called before a WebSocket frame is sent.
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The WebSocket frame to be sent.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 1.15
	 */
	@Override
	public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a WebSocket frame was sent to the server
	 * (but not flushed yet).
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The sent frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called when a WebSocket frame was not sent to the server
	 * because a <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1"
	 * >close frame</a> has already been sent.
	 * <p>
	 * <p>
	 * Note that {@code onFrameUnsent} is not called when {@link
	 * #onSendError(WebSocket, WebSocketException, WebSocketFrame)
	 * onSendError} is called.
	 * </p>
	 *
	 * @param websocket The WebSocket.
	 * @param frame     The unsent frame.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

	}

	/**
	 * Called between after a thread is created and before the thread's
	 * {@code start()} method is called.
	 *
	 * @param websocket  The WebSocket.
	 * @param threadType The thread type.
	 * @param thread     The newly created thread instance.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 2.0
	 */
	@Override
	public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

	}

	/**
	 * Called at the very beginning of the thread's {@code run()} method implementation.
	 *
	 * @param websocket  The WebSocket.
	 * @param threadType The thread type.
	 * @param thread     The thread instance.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 2.0
	 */
	@Override
	public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

	}

	/**
	 * Called at the very end of the thread's {@code run()} method implementation.
	 *
	 * @param websocket  The WebSocket.
	 * @param threadType The thread type.
	 * @param thread     The thread instance.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 2.0
	 */
	@Override
	public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

	}

	/**
	 * Call when an error occurred. This method is called before
	 * an <code>on<i>Xxx</i>Error</code> method is called.
	 *
	 * @param websocket The WebSocket.
	 * @param cause     An exception that represents the error.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when a WebSocket frame failed to be read from the WebSocket.
	 * <p>
	 * <p>
	 * Some WebSocket server implementations close a WebSocket connection without sending
	 * a <a href="https://tools.ietf.org/html/rfc6455#section-5.5.1">close frame</a> to a
	 * client in some cases. Strictly speaking, this behavior is a violation against the
	 * specification (<a href="https://tools.ietf.org/html/rfc6455">RFC 6455</a>). However,
	 * this library has allowed the behavior by default since the version 1.29. Even if
	 * the end of the input stream of a WebSocket connection were reached without a
	 * close frame being received, it would trigger neither {@link #onError(WebSocket, * WebSocketException) onError()} method nor {@link #onFrameError(WebSocket, * WebSocketException, WebSocketFrame) onFrameError()} method. If you want to make
	 * this library report an error in the case, pass {@code false} to {@link
	 * WebSocket#setMissingCloseFrameAllowed(boolean)} method.
	 * </p>
	 *
	 * @param websocket The WebSocket.
	 * @param cause     An exception that represents the error. When the error occurred
	 *                  because of {@link InterruptedIOException InterruptedIOException},
	 *                  {@code exception.getError()} returns {@link WebSocketError#INTERRUPTED_IN_READING}.
	 *                  For other IO errors, {@code exception.getError()} returns {@link
	 *                  WebSocketError#IO_ERROR_IN_READING}. Other error codes denote
	 *                  protocol errors, which imply that some bugs may exist in either
	 *                  or both of the client-side and the server-side implementations.
	 * @param frame     The WebSocket frame. If this is not {@code null}, it means that
	 *                  verification of the frame failed.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when it failed to concatenate payloads of multiple frames
	 * to construct a message. The reason of the failure is probably
	 * out-of-memory.
	 *
	 * @param websocket The WebSocket.
	 * @param cause     An exception that represents the error.
	 * @param frames    The list of frames that form a message. The first element
	 *                  is either a text frame and a binary frame, and the other
	 *                  frames are continuation frames.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when a message failed to be decompressed.
	 *
	 * @param websocket  The WebSocket.
	 * @param cause      An exception that represents the error.
	 * @param compressed The compressed message that failed to be decompressed.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 1.16
	 */
	@Override
	public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when it failed to convert payload data into a string.
	 * The reason of the failure is probably out-of-memory.
	 *
	 * @param websocket The WebSocket.
	 * @param cause     An exception that represents the error.
	 * @param data      The payload data that failed to be converted to a string.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when an error occurred when a frame was tried to be sent
	 * to the server.
	 *
	 * @param websocket The WebSocket.
	 * @param cause     An exception that represents the error.
	 * @param frame     The frame which was tried to be sent. This is {@code null}
	 *                  when the error code of the exception is {@link
	 *                  WebSocketError#FLUSH_ERROR FLUSH_ERROR}.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when an uncaught throwable was detected in either the
	 * reading thread (which reads frames from the server) or the
	 * writing thread (which sends frames to the server).
	 *
	 * @param websocket The WebSocket.
	 * @param cause     The cause of the error.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 */
	@Override
	public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called when an <code>on<i>Xxx</i>()</code> method threw a {@code Throwable}.
	 *
	 * @param websocket The WebSocket.
	 * @param cause     The {@code Throwable} an <code>on<i>Xxx</i></code> method threw.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is just ignored.
	 * @since 1.9
	 */
	@Override
	public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
		Logger.trace(this, "Twitch IRC (WebSocket) Exception:" + cause);
	}

	/**
	 * Called before an opening handshake is sent to the server.
	 *
	 * @param websocket   The WebSocket.
	 * @param requestLine The request line. For example, {@code "GET /chat HTTP/1.1"}.
	 * @param headers     The HTTP headers.
	 * @throws Exception An exception thrown by an implementation of this method.
	 *                   The exception is passed to {@link #handleCallbackError(WebSocket, Throwable)}.
	 * @since 1.21
	 */
	@Override
	public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

	}
}
