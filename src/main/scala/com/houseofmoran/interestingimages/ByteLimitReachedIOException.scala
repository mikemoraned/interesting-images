package com.houseofmoran.interestingimages

import java.io.IOException;

class ByteLimitReachedIOException(limit: Int) extends IOException("reached limit: " + limit);
