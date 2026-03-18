package com.pao.laboratory03.bonus;

public enum Status
{
/**1. Status — cu metoda abstractă boolean canTransitionTo(Status next):
 *    - TODO → poate trece la IN_PROGRESS sau CANCELLED
 *    - IN_PROGRESS → poate trece la DONE sau CANCELLED
 *    - DONE → nu poate trece nicăieri
 *    - CANCELLED → nu poate trece nicăieri*/

    TODO
        {
            @Override
            public boolean canTransitionTo(Status next)
            {
                return next==IN_PROGRESS || next==CANCELLED;
            }
        },
    IN_PROGRESS
            {
                @Override
                public boolean canTransitionTo(Status next)
                {
                    return next==DONE || next==CANCELLED;
                }
            },
    DONE
            {
                @Override
                public boolean canTransitionTo(Status next)
                {
                    return false;
                }
            },

     CANCELLED
             {
                 @Override
                 public boolean canTransitionTo(Status next)
                 {
                     return false;
                 }
             };
    public abstract boolean canTransitionTo(Status next);
}