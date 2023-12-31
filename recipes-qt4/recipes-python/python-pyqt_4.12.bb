SUMMARY = "Python Qt4 Bindings"
AUTHOR = "Phil Thomson @ riverbank.co.uk"
HOMEPAGE = "http://riverbankcomputing.co.uk"
SECTION = "devel/python"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "\
    file://LICENSE;md5=8bce67d3de90b75ae6e185c5c2ea1604 \
"
DEPENDS = "sip sip-native qt4-x11-free python"

SRC_URI = "\
    https://sourceforge.net/projects/pyqt/files/PyQt4/PyQt-4.12/PyQt4_gpl_x11-${PV}.tar.gz \
"
SRC_URI[md5sum] = "eb8c338f2d8842aa7655e8e1aa840bed"
SRC_URI[sha256sum] = "3c1d4b55314adb3e1132de8fc2a92eed216d37e58aceed41294dbca210ca88db"

S = "${WORKDIR}/PyQt4_gpl_x11-${PV}"

PARALLEL_MAKE = ""

inherit qmake2 pythonnative python-dir features_check
# depends on qt4-x11-free
REQUIRED_DISTRO_FEATURES = "x11"

DISABLED_FEATURES = "PyQt_Desktop_OpenGL PyQt_Accessibility PyQt_SessionManager PyQt_OpenSSL"

DISABLED_FEATURES:append:arm = " PyQt_qreal_double"

PYQT_MODULES = "QtCore QtGui QtDeclarative QtNetwork QtSvg QtWebKit"
PYQT_MODULES:aarch64 = "QtCore QtGui QtDeclarative QtNetwork QtSvg"

do_configure() {
    echo "py_platform = linux" > pyqt.cfg
    echo "py_inc_dir = %(sysroot)/$includedir/python%(py_major).%(py_minor)" >> pyqt.cfg
    echo "py_pylib_dir = %(sysroot)/${libdir}/python%(py_major).%(py_minor)" >> pyqt.cfg
    echo "py_pylib_lib = python%(py_major).%(py_minor)mu" >> pyqt.cfg
    echo "pyqt_module_dir = ${D}/${libdir}/python%(py_major).%(py_minor)/site-packages" >> pyqt.cfg
    echo "pyqt_bin_dir = ${D}/${bindir}" >> pyqt.cfg
    echo "pyqt_sip_dir = ${D}/${datadir}/sip/PyQt4" >> pyqt.cfg
    echo "pyuic_interpreter = ${D}/${bindir}/python%(py_major).%(py_minor)" >> pyqt.cfg
    echo "pyqt_disabled_features = ${DISABLED_FEATURES}" >> pyqt.cfg
    echo "qt_shared = True" >> pyqt.cfg
    echo "[Qt 4.8]" >> pyqt.cfg
    echo "pyqt_modules = ${PYQT_MODULES}" >> pyqt.cfg
    echo yes | python configure-ng.py --verbose --qmake  ${STAGING_BINDIR_NATIVE}/qmake2 --configuration pyqt.cfg --sysroot ${STAGING_DIR_HOST}
}
do_install() {
     oe_runmake install
}

RDEPENDS:${PN} = "python-core python-sip"

FILES:${PN} += "${libdir}/${PYTHON_DIR}/site-packages ${datadir}/sip/PyQt4/"
FILES:${PN}-dbg += "${libdir}/${PYTHON_DIR}/site-packages/*/.debug/"

