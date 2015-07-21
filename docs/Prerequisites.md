## Pre-requisites


### Client Pre-requisites

Web Apollo 2.0 is a web-based application, so the only client side
requirement is a web browser. Web Apollo has been tested on Chrome, Firefox, and Safari
and matches the web browser requirements for JBrowse (see [jbrowse.org](http://jbrowse.org) for details).

### Server-side Pre-requisites

Note: see the [Web Apollo 2.0 quick-start](Apollo2Build.md) for the
quickest way to take care of pre-requisites.

-   System pre-requisites (see quick-start guide for simple setup)
    -   Any Unix like system (e.g., Unix, Linux, Mac OS X)
    -   Servlet container (must support servlet spec 3.0+) such as tomcat 7
    -   Java 7+
    -   Grails (easiest way to install is using GVM, see [Web Apollo 2.0 quick-start](Apollo2Build.md) for this step)
    -   Ant 1.8+ (most package managers will have this)
    -   A database (RDMS) system. Sample configurations for PostgreSQL and MySQL are available. H2 configuration does not require any manual installation.
    -   Basic tools like Git, Curl, a text editor, etc
-   Data generation pipeline pre-requisites (for full list see http://gmod.org/wiki/JBrowse_Configuration_Guide)
    -   System packages
        -   libpng12-0 (optional, for JBrowse imagetrack)
        -   libpng12-dev (optional, for JBrowse imagetrack)
        -   zlib1g (Debian/Ubuntu)
        -   zlib1g-dev (Debian/Ubuntu)
        -   zlib (RedHat/CentOS)
        -   zlib-devel (RedHat/CentOS)
        -   libexpat1-dev (Debian/Ubuntu)
        -   expat-dev (RedHat/CentOS)
-   Perl pre-requisites:
    -   Web Apollo will automatically try to install all perl-pre-requisites
    -   If you are building Web Apollo in "release" mode, perl 5.10 or up will be required
-   Sequence search (optional)
    -   Blat (download [Linux](http://hgdownload.cse.ucsc.edu/admin/exe/linux.x86_64/) or [OSX](http://hgdownload.cse.ucsc.edu/admin/exe/macOSX.x86_64/) binaries)

#### Package manager commands

To install all system pre-requisites, you can try the following commands


##### Debian/Ubuntu

`sudo apt-get install openjdk-7-jdk curl libexpat1-dev postgresql postgresql-server-dev-all maven tomcat7 git`

##### CentOS/RedHat

`sudo yum install postgresql postgresql-server postgresql-devel maven expat-devel tomcat git curl`

##### MacOSX/Homebrew

`brew install maven postgresql wget tomcat git`
