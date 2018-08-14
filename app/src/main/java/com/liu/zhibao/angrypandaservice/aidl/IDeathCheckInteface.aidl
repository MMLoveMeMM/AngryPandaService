// IDeathCheckInteface.aidl
package com.liu.zhibao.angrypandaservice.aidl;

// Declare any non-default types here with import statements

interface IDeathCheckInteface {
    void checkDeath(String data);
    void setBinder(IBinder client);
}