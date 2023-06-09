// fp.java

public class fp
{
	// Be sure to put your name on this next line...
	public String myName()
	{
		return "Kien Nguyen";
	}

	public int add(int a, int b)
	{
		FPNumber fa = new FPNumber(a);
		FPNumber fb = new FPNumber(b);
		FPNumber result = new FPNumber(0);
		// Put your code in here!

		//Handle NaN exception
		//Return NaN value if found
		if (fa.isNaN())
		{
			return a;
		}
		if (fb.isNaN())
		{
			return b;
		}

		//Handle zero exceptions
		//If one number is zero, return the other input
		if (fa.isZero())
		{
			return b;
		}
		if (fb.isZero())
		{
			return a;
		}

		//Check infinity exceptions
		if (fa.isInfinity() && fb.isInfinity())
		{
			//Check if signs are same
			if (fa.s() == fb.s())
			{
				return a;
			}
			else
			//Return NaN if signs aren't the same
			{
				fa.setF(1);
				return a;
			}
		}
		//If either input is infinity, return infinity
		if (fa.isInfinity() && !fb.isInfinity())
		{
			return a;
		}
		if (fb.isInfinity() && !fa.isInfinity())
		{
			return b;
		}

		//Sort numbers
		FPNumber sigA = new FPNumber(0);
		FPNumber sigB = new FPNumber(0);
		if (Math.abs(Float.intBitsToFloat(fa.asInt())) > Math.abs(Float.intBitsToFloat(fb.asInt())))
		{
			sigA.setS(fa.s());
			sigA.setE(fa.e());
			sigA.setF(fa.f());
			sigB.setS(fb.s());
			sigB.setE(fb.e());
			sigB.setF(fb.f());
		}
		else
		{
			sigA.setS(fb.s());
			sigA.setE(fb.e());
			sigA.setF(fb.f());
			sigB.setS(fa.s());
			sigB.setE(fa.e());
			sigB.setF(fa.f());
		}

		//Align exponents
		while (sigB.e() != sigA.e())
		{
			if (sigA.e() - sigB.e() > 24)
			{
				if (Math.abs(fa.asInt()) > Math.abs(fb.asInt()))
				{
					return fa.asInt();
				}
				else
				{
					return fb.asInt();
				}
			}
			sigB.setF(sigB.f() >> 1);
			sigB.setE(sigB.e() + 1);
			
		}

		//Add/Subtract the mantissas
		if (sigA.s() == sigB.s())
		{
			result.setF(sigA.f() + sigB.f());
		}
		else
		{
			result.setF(sigA.f() - sigB.f());
		}

		result.setS(sigA.s());

		//Check if mantissa equals 0
		if (result.f() == 0)
		{
			return 0;
		}

		result.setE(sigA.e());

		//Normalize
		//Check if 27th bit is set
		if (result.f() >= 67108864)
		{
			result.setF(result.f() >> 1);
			result.setE(result.e() + 1);
			//Check if exponent reaches overflow, return infinity
			if (result.e() >= 255)
			{
				result.setE(255);
				result.setF(0);
				return result.asInt();
			}
		}
		//Check if 26th bit is not set
		while (result.f() < 33554432)
		{
			result.setF(result.f() << 1);
			result.setE(result.e() - 1);
			//Check if exponent reaches underflow, return denormalized number
			if (result.e() == 0)
			{
				result.setF(result.f() >> 1);
				return result.asInt();
			}
		}

		return result.asInt();
	}

	public int mul(int a, int b)
	{
		FPNumber fa = new FPNumber(a);
		FPNumber fb = new FPNumber(b);
		FPNumber result = new FPNumber(0);

		// Put your code in here!

		//Handle NaN exception
		//Return NaN value if found
		if (fa.isNaN())
		{
			return a;
		}
		if (fb.isNaN())
		{
			return b;
		}

		//If one input is 0 and other is infinity, return NaN
		if ((fa.isInfinity() && fb.isZero()) || (fb.isInfinity() && fa.isZero()))
		{
			result.setE(255);
			result.setF(255);
			return result.asInt();
		}

		//If either input is 0, return 0
		if (fa.isZero() || fb.isZero())
		{
			return 0;
		}

		//If either input is infinity, return infinity
		if (fa.isInfinity() && fb.isInfinity())
		{
			if (fa.s() == -1)
			{
				return a;
			}
			if (fb.s() == -1)
			{
				return b;
			}
			return a;
		}
		if (fa.isInfinity())
		{
			return a;
		}
		if (fb.isInfinity())
		{
			return b;
		}

		//Get sign of result
		if (fa.s() != fb.s())
		{
			result.setS(-1);
		}
		else
		{
			result.setS(1);
		}

		//Add exponents
		result.setE(fa.e() + fb.e());
		result.setE(result.e() - 127);
		//Check if exponent is overflow
		if (result.e() > 255)
		{
			result.setF(0);
			return result.asInt();
		}
		//Check if exponent is underflow
		if (result.e() < 0)
		{
			result.setF(0);
			return result.asInt();
		}

		//Multiply mantissas
		result.setF(fa.f() * fb.f());
		//Shift 52-bit value to 26-bit value
		result.setF(result.f() >> 26);
		result.setE(result.e() + 1);

		//Normalize
		//Check if 27th bit is set
		if (result.f() >= 67108864)
		{
			result.setF(result.f() >> 1);
			result.setE(result.e() + 1);
			//Check if exponent reaches overflow, return infinity
			if (result.e() >= 255)
			{
				result.setE(255);
				result.setF(0);
				return result.asInt();
			}
		}
		//Check if 26th bit is not set
		while (result.f() < 33554432)
		{
			result.setF(result.f() << 1);
			result.setE(result.e() - 1);
			//Check if exponent reaches underflow, return denormalized number
			if (result.e() == 0)
			{
				result.setF(result.f() >> 1);
				return result.asInt();
			}
		}

		return result.asInt();
	}

	// Here is some test code that one student had written...
	public static void main(String[] args)
	{
		int v24_25	= 0x41C20000; // 24.25
		int vN24_25	= 0xc1c20000; // -24.25
		int v_1875	= 0xBE400000; // -0.1875
		int v5		= 0xC0A00000; // -5.0
		int v0		= 0x00000000; // 0
		int vNaN	= 0xffc20000; // NaN
		int vInf	= 0x7f800000; // +infinity


		fp m = new fp();

		System.out.println("Addition: ");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, v_1875)) + " should be 24.0625");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, v5)) + " should be 19.25");
		System.out.println(Float.intBitsToFloat(m.add(v_1875, v5)) + " should be -5.1875");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, v0)) + " should be 24.25");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, vN24_25)) + " should be 0.0");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, vNaN)) + " should be NaN");
		System.out.println(Float.intBitsToFloat(m.add(v24_25, vInf)) + " should be Infinity");
		System.out.println();

		System.out.println("Multiplication: ");
		System.out.println(Float.intBitsToFloat(m.mul(v24_25, v_1875)) + " should be -4.546875");
		System.out.println(Float.intBitsToFloat(m.mul(v24_25, v5)) + " should be -121.25");
		System.out.println(Float.intBitsToFloat(m.mul(v_1875, v5)) + " should be 0.9375");
		System.out.println(Float.intBitsToFloat(m.mul(v24_25, vNaN)) + " should be NaN");
		System.out.println(Float.intBitsToFloat(m.mul(v0, vInf)) + " should be NaN");
		System.out.println(Float.intBitsToFloat(m.mul(v0, vInf)) + " should be NaN");
		System.out.println(Float.intBitsToFloat(m.mul(v0, v24_25)) + " should be 0.0");
		System.out.println(Float.intBitsToFloat(m.mul(vInf, v24_25)) + " should be Infinity");
	}
}
