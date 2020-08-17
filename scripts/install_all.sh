#
# Copyright (C) 2020 Mike Hummel (mh@mhus.de)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


function inst {
 echo "============================"
 echo $1
 echo "============================"
 if [ ! -d $1 ]; then
   echo Not exists
   return
 fi
 if [ $doAll = 0 ]; then
     lastUpdate=$(stat -f "%m" $1)
     cd $1
     lastEntry=$(find . -type f -not -path  "*/target/*" -not -path "*/.*"  -exec stat -f "%m %N" \{\} + | sort -r | head -n 1)
     lastFile=$(echo $lastEntry|cut -d ' ' -f 2)
     lastModify=$(echo $lastEntry|cut -d ' ' -f 1)
     if [ $lastUpdate = $lastModify ]; then
       echo Not modified
       cd ..
       return
     fi
     echo Modified $lastFile
 else
    cd $1
 fi
 echo mvn $opt
 mvn $opt || exit 1
 cd ..

 if [ $doAll = 0 ]; then
  lastDate=$(date -r $lastModify +"%Y%m%d%H%M.%S")
  touch -t $lastDate $1
 else
  touch $1
 fi
}

export doAll=0

if [ -z "$@" ]; then
  opt="install"
else
  if [ "$1" = "all" ]; then
    doAll=1
    echo "Compile all"
    shift
  fi
  opt="$@"
fi


inst mhus-parent
inst mhus-lib
inst mhus-osgi-tools
inst mhus-osgi-crypt
inst mhus-osgi-servlets
inst mhus-transform
inst mhus-vaadin
inst mhus-persistence
inst mhus-mongo
inst mhus-rest
inst mhus-micro
inst mhus-dev

inst cherry-reactive
inst cherry-vault
inst cherry-web

