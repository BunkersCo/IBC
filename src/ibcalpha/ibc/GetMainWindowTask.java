// This file is part of IBC.
// Copyright (C) 2004 Steven M. Kearns (skearns23@yahoo.com )
// Copyright (C) 2004 - 2018 Richard L King (rlking@aultan.com)
// For conditions of distribution and use, see copyright notice in COPYING.txt

// IBC is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// IBC is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with IBC.  If not, see <http://www.gnu.org/licenses/>.

package ibcalpha.ibc;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;

class GetMainWindowTask implements Callable<JFrame>{
    private volatile JFrame mMainWindow;
    private final Lock lock = new ReentrantLock();
    private final Condition gotFrame = lock.newCondition();

    @Override
    public JFrame call() throws InterruptedException {
        lock.lock();
        try {
            while (mMainWindow == null) {
                gotFrame.await();
            }
        } finally {
            lock.unlock();
        }
        return mMainWindow;
    }  

    void setMainWindow(JFrame window) {
        lock.lock();
        try {
            mMainWindow = window;
            gotFrame.signal();
        } finally {
            lock.unlock();
        }
    }

}
