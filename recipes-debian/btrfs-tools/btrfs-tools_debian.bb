# base recipe : meta/recipes-devtools/btrfs-tools/btrfs-tools_4.20.1.bb
# base branch : warrior
# base commit : 243655b6c22ced795d79c888fd0f01608300dfaa

SUMMARY = "Checksumming Copy on Write Filesystem utilities"
DESCRIPTION = "Btrfs is a new copy on write filesystem for Linux aimed at \
implementing advanced features while focusing on fault tolerance, repair and \
easy administration. \
This package contains utilities (mkfs, fsck, btrfsctl) used to work with \
btrfs and an utility (btrfs-convert) to make a btrfs filesystem from an ext3."

inherit debian-package
require recipes-debian/sources/btrfs-progs.inc
DEBIAN_UNPACK_DIR = "${WORKDIR}/btrfs-progs-v${PV}"

HOMEPAGE = "https://btrfs.wiki.kernel.org"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=fcb02dc552a041dee27e4b85c7396067"
SECTION = "base"
DEPENDS = "util-linux attr e2fsprogs lzo acl python3-setuptools-native"
DEPENDS_append_class-target = " udev"
RDEPENDS_${PN} = "libgcc"

FILESPATH_append = ":${COREBASE}/meta/recipes-devtools/btrfs-tools/btrfs-tools"
SRC_URI += "\
           file://python3-use-deb-layout_revert.patch \
           file://0001-Makefile-build-mktables-using-native-gcc.patch \
           file://0001-Add-LDFLAGS-when-building-libbtrfsutil.so.patch \
           file://0001-Add-a-possibility-to-specify-where-python-modules-ar.patch \
           "

inherit autotools-brokensep pkgconfig manpages distutils3-base

CLEANBROKEN = "1"

PACKAGECONFIG[manpages] = "--enable-documentation, --disable-documentation, asciidoc-native xmlto-native"
EXTRA_OECONF = " --disable-zstd"
EXTRA_OECONF_append_libc-musl = " --disable-backtrace "

do_configure_prepend() {
	# Upstream doesn't ship this and autoreconf won't install it as automake isn't used.
	mkdir -p ${S}/config
	cp -f $(automake --print-libdir)/install-sh ${S}/config/
}

do_install_append() {
    oe_runmake 'DESTDIR=${D}' 'PYTHON_SITEPACKAGES_DIR=${PYTHON_SITEPACKAGES_DIR}' install_python
}

BBCLASSEXTEND = "native"
