/*********************************************************************
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* PaceCalculator is distributed in the hope that it will be
* useful, but WITHOUT ANY WARRANTY; without even the implied
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.
* If not, see <http://www.gnu.org/licenses/>.
*********************************************************************/
package rosas.lou.clock;

import java.util.*;
import java.time.*;
import rosas.lou.clock.*;

public interface ClockSubscriber{
   public void update(String time);
   public void update(java.time.Instant instant);
   public void update(boolean isRunning);
   public void update(String time, String type);
   public void update(List<?> list);
}
