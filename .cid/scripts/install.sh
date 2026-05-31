#!/usr/bin/env bash
set -euo pipefail

# parameters
CID_VERSION="${1:?Error: CID_VERSION must be provided}"
CID_SHA256="${2:?Error: CID_SHA256 must be provided for integrity check}"
GPG_FINGERPRINT="${3:-}"

# variables
TMP_DIR="$(mktemp -d)"
BIN_DIR="/usr/local/bin"
BASE_URL="${CID_MIRROR_URL:-https://github.com/cidverse/cid/releases/download}"
BINARY_URL="${BASE_URL}/v${CID_VERSION}/linux_amd64"
SIG_URL="${BINARY_URL}.asc"

# github actions
if [[ "${GITHUB_ACTIONS:-}" == "true" ]]; then
    echo "Detected GitHub Actions ..."

    TOOL_DIR="${RUNNER_TOOL_CACHE}/cid-${CID_SHA256}"
    BIN_DIR="${TOOL_DIR}/bin"
fi

# download
echo "Fetching binary and signature [$CID_VERSION]..."
curl -sSL -o "$TMP_DIR/cid" "$BINARY_URL"
curl -sSL -o "$TMP_DIR/cid.asc" "$SIG_URL"

# sha256 integrity
if [[ -n "${CID_SHA256}" ]]; then
    ACTUAL_SHA256=$(sha256sum "$TMP_DIR/cid" | awk '{print $1}')

    if [[ "${ACTUAL_SHA256}" == "${CID_SHA256}" ]]; then
        echo "✅ CID Checksum verification passed. checksum=${ACTUAL_SHA256}"
    else
        echo "❌ CHECKSUM VERIFICATION FAILED!"
        echo "Expected: ${CID_SHA256}"
        echo "Actual:   ${ACTUAL_SHA256}"
        exit 1
    fi
fi

# gpg verification
if [[ -n "${GPG_FINGERPRINT}" ]]; then
    if [[ ${#GPG_FINGERPRINT} -lt 40 ]]; then
        echo "❌ Error: GPG_FINGERPRINT is too short (${#GPG_FINGERPRINT}/40 chars)."
        echo "Please provide the full 40-character hex fingerprint."
        exit 1
    fi

    echo "Fetching GPG key ${GPG_FINGERPRINT}..."
    if ! gpg --keyserver hkps://keyserver.ubuntu.com --quiet --recv-keys "$GPG_FINGERPRINT" 2>/dev/null; then
        echo "❌ Error: Could not retrieve key ${GPG_FINGERPRINT} from keyserver."
        exit 1
    fi

    GPG_OUTPUT=$(gpg --verify "$TMP_DIR/cid.asc" "$TMP_DIR/cid" 2>&1)
    GPG_STATUS=$?
    if [ $GPG_STATUS -eq 0 ]; then
        echo "✅ GPG verification successful!"
        echo "Key Fingerprint: ${GPG_FINGERPRINT}"
        echo "$(echo "$GPG_OUTPUT" | grep "Good signature from")"
    else
        echo "❌ GPG VERIFICATION FAILED!"
        echo "---------------------------------------------------"
        echo "$GPG_OUTPUT"
        echo "---------------------------------------------------"
        echo "Possible issues: The file was tampered with, or the signature"
        echo "was not created by the key ${GPG_FINGERPRINT}."
        exit 1
    fi
fi

# install binary
mkdir -p "$BIN_DIR"
install -m 755 "$TMP_DIR/cid" "$BIN_DIR/cid"

# github actions
if [[ "${GITHUB_ACTIONS:-}" == "true" ]]; then
    echo "${BIN_DIR}" >> "$GITHUB_PATH"
    echo "CID_VERSION=${CID_VERSION}" >> "$GITHUB_ENV"
fi

# export to path
export PATH="${BIN_DIR}:${PATH}"
echo "CID version: ${CID_VERSION}"
